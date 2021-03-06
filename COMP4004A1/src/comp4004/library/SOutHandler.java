package comp4004.library;

import comp4004.library.SOutput;
import comp4004.library.UtilConfig;
import comp4004.library.FeeTable;
import comp4004.library.LoanTable;

import java.util.Date;

import comp4004.library.ItemTable;
import comp4004.library.TitleTable;
import comp4004.library.UserTable;

public class SOutHandler {
	//Wait States
	public static final int WAIT = 0;
	public static final int FINISHWAIT = 1;
	//Clerk States
	public static final int CLERK = 10;
	public static final int CLERKLOGIN = 11;
	public static final int MONITOR = 12;
	//Creation States
	public static final int CREATEUSER = 20;
	public static final int CREATETITLE = 21;
	public static final int CREATEITEM = 22;
	//Deletion States
	public static final int DELETEUSER = 30;
	public static final int DELETETITLE = 31;
	public static final int DELETEITEM = 32;
	//User States
	public static final int USER = 40;
	public static final int USERLOGIN = 41;
	//User Option States
	public static final int BORROW = 50;
	public static final int RENEW = 51;
	public static final int RETURN = 52;
	public static final int PAYFINE = 53;
	
	public SOutput clerkLogin(String input) {
		SOutput out = new SOutput("", 0);
		
		if (input.equalsIgnoreCase(UtilConfig.CLERK_PASSWORD)) {
			out.setOutput("What can I do for you? Menu: Create User/Title/Item, Delete User/Title/Item.");
			out.setState(CLERK);
		} else {
			out.setOutput("Wrong Password. Please Input the Password");
			out.setState(CLERKLOGIN);
		}
		return out;
	}
	
	public SOutput createUser(String input) {
		SOutput out = new SOutput("" , 0);
		String[] strArray = null;   
        strArray = input.split(",");
        boolean email=strArray[0].contains("@");
        Object result="";
        if(strArray.length!=2 || email!=true){
        	out.setOutput("Your input should in this format: 'username,password'");
        	out.setState(CREATEUSER);
        }else{
        	result=UserTable.getInstance().createuser(strArray[0], strArray[1]);
        	if(result.equals(true)){
        		out.setOutput("Success!");
        	}else{
        		out.setOutput("The User Already Exists!");
        	}
        	out.setState(CLERK);
        }
		return out;
	}
	
	public SOutput createTitle(String input) {
		SOutput output=new SOutput("",0);
		String[] strArray = null;   
        strArray = input.split(",");
        boolean number=isInteger(strArray[0]);
        Object result="";
        if(strArray.length!=2 || number!=true){
        	output.setOutput("Your input should in this format:'ISBN,title',ISBN should be a 13-digit number");
        	output.setState(CREATETITLE);
        }else{
        	result=TitleTable.getInstance().createtitle(strArray[0], strArray[1]);
        	if(result.equals(true)){
        		output.setOutput("Success!");
        	}else{
        		output.setOutput("The Title Already Exists!");
        	}
        	output.setState(CLERK);
        }
		return output;
	}
	
	public SOutput createItem(String input) {
		SOutput output=new SOutput("",0);
		String[] strArray = null;   
        strArray = input.split(",");
        boolean number=isInteger(strArray[0]);
        Object result="";
        if(strArray.length!=1 || number!=true){
        	output.setOutput("Your input should in this format:'ISBN',ISBN should be a 13-digit number");
        	output.setState(CREATEITEM);
        }else{
        	result=ItemTable.getInstance().createitem(strArray[0]);
        	if(result.equals(true)){
        		output.setOutput("Success!");
        	}else{
        		output.setOutput("The Title Does Not Exists!");
        	}
        	output.setState(CLERK);
        }
		return output;
	}
	
	public SOutput deleteUser(String input) {
		SOutput output=new SOutput("",0);
		String[] strArray = null;   
        strArray = input.split(",");
        int userid=UserTable.getInstance().lookup(strArray[0]);
        boolean email=strArray[0].contains("@");
        Object result="";
        if(strArray.length!=1 || email!=true){
        	output.setOutput("Your input should in this format:'useremail'");
        	output.setState(DELETEUSER);
        }else if(userid==-1){
        	output.setOutput("The User Does Not Exist!");
        	output.setState(DELETEUSER);
        }else{
        	result=UserTable.getInstance().delete(userid);
        	if(result.equals("success")){
        		output.setOutput("Success!");
        	}else{
        		output.setOutput(result+"!");
        	}
        	output.setState(CLERK);
        }
		return output;
	}
	
	public SOutput deleteTitle(String input) {
		SOutput output=new SOutput("",0);
		String[] strArray = null;   
        strArray = input.split(",");
        boolean number=isInteger(strArray[0]);
        Object result="";
        if(strArray.length!=1 || number!=true){
        	output.setOutput("Your input should in this format:'ISBN',ISBN should be a 13-digit number");
        	output.setState(DELETETITLE);
        }else{
        	result=TitleTable.getInstance().delete(strArray[0]);
        	if(result.equals("success")){
        		output.setOutput("Success!");
        	}else{
        		output.setOutput(result+"!");
        	}
        	output.setState(CLERK);
        }
		return output;
	}
	
	public SOutput deleteItem(String input) {
		SOutput output=new SOutput("",0);
		String[] strArray = null;   
        strArray = input.split(",");
        boolean number=isInteger(strArray[0]);
        Object result="";
        if(strArray.length!=2 || number!=true){
        	output.setOutput("Your input should in this format:'ISBN,copynumber',ISBN should be a 13-digit number");
        	output.setState(DELETEITEM);
        }else{
        	boolean copynumber=isNumber(strArray[1]);
        	if(copynumber!=true){
        		output.setOutput("Your input should in this format:'ISBN,copynumber',ISBN should be a 13-digit number");
            	output.setState(DELETEITEM);
        	}else{
        		result=ItemTable.getInstance().delete(strArray[0], strArray[1]);
            	if(result.equals("success")){
            		output.setOutput("Success!");
            	}else{
            		output.setOutput(result+"!");
            	}
            	output.setState(CLERK);
        	}
        }
		return output;
	}
	
	public SOutput monitor() {
		SOutput output = new SOutput("",0);
		String temp = "";
		//Add users
		temp+="Users:\n\n";
		for (int i=0; i<UserTable.getInstance().getUserTable().size(); i++) {
			temp+= UserTable.getInstance().getUserTable().get(i).getUsername() + "\n"; 
		}
		
		//Add Books
		temp+="\nBooks\n\n";
		for (int j=0; j<TitleTable.getInstance().getTitleTable().size(); j++) {
			temp+=TitleTable.getInstance().getTitleTable().get(j).getBooktitle()+" - "+TitleTable.getInstance().getTitleTable().get(j).getISBN()+"\n";
		}
		output.setOutput(temp);
		output.setState(CLERK);
		return output;
	}
	
	public SOutput userLogin(String input) {
		SOutput output=new SOutput("",0);
		String[] strArray = null;   
        strArray = input.split(",");
        boolean email=strArray[0].contains("@");
        int result=0;
        if(strArray.length!=2 || email!=true){
        	output.setOutput("Your input should in this format:'username,password'");
        	output.setState(USERLOGIN);
        }else{
        	result=UserTable.getInstance().checkUser(strArray[0], strArray[1]);
        	if(result==0){
        		output.setOutput("What can I do for you?Menu:Borrow,Renew,Return,Pay Fine.");
            	output.setState(USER);
        	}else if(result==1){
        		output.setOutput("Wrong Password!Please Input Username and Password:'username,password'");
            	output.setState(USERLOGIN);
        	}else{
        		output.setOutput("The User Does Not Exist!Please The Username and Password:'username,password'");
            	output.setState(USERLOGIN);
        	}
        }
		return output;
	}
	
	public SOutput borrow(String input) {
		SOutput output=new SOutput("",0);
		String[] strArray = null;   
        strArray = input.split(",");
        boolean email=strArray[0].contains("@");
        int userid=UserTable.getInstance().lookup(strArray[0]);
        Object result="";
        if(strArray.length!=3 || email!=true){
        	output.setOutput("Your input should in this format:'useremail,ISBN,copynumber'");
        	output.setState(BORROW);
        }else if(userid==-1){
        	output.setOutput("The User Does Not Exist!");
        	output.setState(BORROW);
        }else{
        	boolean ISBN=isInteger(strArray[1]);
        	boolean copynumber=isNumber(strArray[2]);
        	if(ISBN!=true || copynumber!=true){
        		output.setOutput("Your input should in this format:'useremail,ISBN,copynumber'");
            	output.setState(BORROW);
        	}else{
        		result=LoanTable.getInstance().createloan(userid, strArray[1], strArray[2], new Date());
        		if(result.equals("success")){
            		output.setOutput("Success!");
            	}else{
            		output.setOutput(result+"!");
            	}
        	}
        	output.setState(USER);
        }
		return output;
	}
	
	public SOutput renew(String input) {
		SOutput output=new SOutput("",0);
		String[] strArray = null;   
        strArray = input.split(",");
        boolean email=strArray[0].contains("@");
        int userid=UserTable.getInstance().lookup(strArray[0]);
        Object result="";
        if(strArray.length!=3 || email!=true){
        	output.setOutput("Your input should in this format:'useremail,ISBN,copynumber'");
        	output.setState(RENEW);
        }else if(userid==-1){
        	output.setOutput("The User Does Not Exist!");
        	output.setState(RENEW);
        }else{
        	boolean ISBN=isInteger(strArray[1]);
        	boolean copynumber=isNumber(strArray[2]);
        	if(ISBN!=true || copynumber!=true){
        		output.setOutput("Your input should in this format:'useremail,ISBN,copynumber'");
            	output.setState(RENEW);
        	}else{
        		result=LoanTable.getInstance().renewal(userid, strArray[1], strArray[2], new Date());
        		if(result.equals("success")){
            		output.setOutput("Success!");
            	}else{
            		output.setOutput(result+"!");
            	}
        	}
        	output.setState(USER);
        }
		return output;
	}
	
	public SOutput returnBook(String input) {
		SOutput output=new SOutput("",0);
		String[] strArray = null;   
        strArray = input.split(",");
        boolean email=strArray[0].contains("@");
        int userid=UserTable.getInstance().lookup(strArray[0]);
        Object result="";
        if(strArray.length!=3 || email!=true){
        	output.setOutput("Your input should in this format:'useremail,ISBN,copynumber'");
        	output.setState(RETURN);
        }else if(userid==-1){
        	output.setOutput("The User Does Not Exist!");
        	output.setState(RETURN);
        }else{
        	boolean ISBN=isInteger(strArray[1]);
        	boolean copynumber=isNumber(strArray[2]);
        	if(ISBN!=true || copynumber!=true){
        		output.setOutput("Your input should in this format:'useremail,ISBN,copynumber'");
            	output.setState(RETURN);
        	}else{
        		result=LoanTable.getInstance().returnItem(userid, strArray[1], strArray[2], new Date());
        		if(result.equals("success")){
            		output.setOutput("Success!");
            	}else{
            		output.setOutput(result+"!");
            	}
        	}
        	output.setState(USER);
        }
		return output;

	}
	
	public SOutput payFine(String input) {
		SOutput output=new SOutput("",0);
		String[] strArray = null;   
        strArray = input.split(",");
        boolean email=strArray[0].contains("@");
        int userid=UserTable.getInstance().lookup(strArray[0]);
        Object result="";
        if(strArray.length!=1 || email!=true){
        	output.setOutput("Your input should in this format:'useremail'");
        	output.setState(PAYFINE);
        }else if(userid==-1){
        	output.setOutput("The User Does Not Exist!");
        	output.setState(PAYFINE);
        }else{
        	result=FeeTable.getInstance().payfine(userid);	
        	if(result.equals("success")){
        		output.setOutput("Success!");
        		}else{
            		output.setOutput(result+"!");
            	}
        		output.setState(USER);
        	}
        	
		return output;
	}
	
	public static boolean isInteger(String value) {
		char[] ch = value.toCharArray();
		boolean isNumber=true;
		if(value.length()==13){
			for (int i = 0; i < ch.length; i++) {
				isNumber = Character.isDigit(ch[i]);
			}
		}else{
			isNumber=false;
		}
		return isNumber;
	}
	
	public boolean isNumber(String value) {
		char[] ch = value.toCharArray();
		boolean isNumber=true;
			for (int i = 0; i < ch.length; i++) {
				isNumber = Character.isDigit(ch[i]);
			}
		return isNumber;
	}
	
}