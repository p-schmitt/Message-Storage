/* Program: Message Client
 * This: messageclient.java
 * Date: 5/12/2016
 * Author: P. Schmitt, R. Hill
 * Purpose: The client side of a simple message server
 */


package messageclient;
import java.net.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

//===========================class MessageClient==========================
//prompts user to either register or sign in.  The login menu runs until a
//successful login is completed.  Then, users are able to add new messages
//to the database and search for previous messages
public class MessageClient {

    public static void main(String[] args) throws Exception 
    {
        Socket clientSocket = new Socket("localhost", 4444);
        String sentence;
        String id="", pass;
        boolean goodLogin = false;
        int loginChoice, menuChoice;
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        
        do
        {
            loginMenu();
            System.out.print("Enter a choice");
            loginChoice= getMenuInput();
            
            if(loginChoice==1)
            {
                sentence="add:";
                System.out.print("Enter user ID: ");
                id = inFromUser.readLine();
                System.out.print("Enter pass: ");
                pass = inFromUser.readLine();
                String idPass = id.concat(":" + pass);
                sentence += idPass;
                outToServer.writeBytes(sentence + '\n');
                String result = inFromServer.readLine();
                    System.out.println("FROM SERVER: " + result);
            }
            else if(loginChoice==2)
            {
                sentence="login:";
                System.out.print("Enter user ID: ");
                id = inFromUser.readLine();
                System.out.print("Enter pass: ");
                pass = inFromUser.readLine();
                String idPass = id.concat(":" + pass);
                sentence += idPass;
                outToServer.writeBytes(sentence + '\n');
                String result = inFromServer.readLine();
                if(result.equals("good"))
                {
                    goodLogin = true;
                }
                System.out.println("FROM SERVER: " + result + " login\n");
            }
            else if(loginChoice==3)
            {
                return;
            }
        }while(goodLogin==false);
        
        while(true)
        {
            mainMenu();
            System.out.print("Enter a choice: ");
            menuChoice = getMenuInput();
            if(menuChoice==1)
            {
                sentence = "insert:"; 
                System.out.print("Type in a message to save: ");
                sentence += inFromUser.readLine()+":"+id;
                outToServer.writeBytes(sentence+'\n');
                        
                String  modifiedSentence = inFromServer.readLine(); 
                System.out.println("FROM SERVER: " + modifiedSentence);
            }
            else if(menuChoice==2)
            {
                sentence="id:";
                System.out.print("Enter message ID: ");
                int messageID = getMenuInput();
                sentence +=messageID+":"+id;
                outToServer.writeBytes(sentence+"\n");
                
                String result = inFromServer.readLine();
                System.out.println("FROM SERVER: " + result);
            }
            
            else if(menuChoice==3)
            {
                sentence ="string:";
                System.out.print("Enter string to search for: ");
                String target = inFromUser.readLine();
                sentence+=target+":"+id;
                outToServer.writeBytes(sentence+"\n");
                
                String result = inFromServer.readLine();
                String[] results = result.split("\t");
                for(String r : results)
                {
                    System.out.println("FROM SERVER: " + r);
                }
            }
            else if(menuChoice==4)
            {
                break;
            }
        }
    }
    
    //===============mainMenu=======================
    //displays main menu as prompt for user input
    private static void mainMenu()
    {
        System.out.println("1. Add new message to database");
        System.out.println("2. Search for message by ID");
        System.out.println("3. Search for message by content");
        System.out.println("4. Quit");
    }
    
    //================loginMenu=======================
    //displays login menu as prompt for user input
    private static void loginMenu()
    {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Quit");
    }
    
    //=================getMenuInput====================
    //method to get user input for various menus
    public static int getMenuInput()
    {
        Scanner input = new Scanner(System.in);
        int menuChoice;
        boolean continueInput = true;
        do
        {
            try
            {
                menuChoice = input.nextInt();
                continueInput = false;
                return menuChoice;
            }
            catch(InputMismatchException ex)
            {
                System.out.println("Invalid choice. Select again!");
                input.nextLine();
            }
        }while(continueInput);
        return -1;
    }
}
    

