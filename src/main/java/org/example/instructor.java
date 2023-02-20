package org.example;

import java.util.Scanner;

import java.io.*;
import java.sql.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
public class instructor {
    static Connection conn = Connect.ConnectDB();
    static Statement stmt = null;
    static Scanner input = new Scanner(System.in);
    public static String user_id="";
    public static String token="'logged in'";

    public static boolean user=false;
    public static void login(){
        String email="",password="";
        while(true){
            System.out.println("enter your email");
            email=input.nextLine();
            System.out.println("enter your password");
            password=input.nextLine();

            String query="select * from instructor where email='"+email+"' and password='"+password+"';";
            try {
                stmt=conn.createStatement();
                ResultSet rs;

                rs=stmt.executeQuery(query);
                int f=0;
                while(rs.next()){
                    f++;
                    user_id=rs.getString(1);
                }
                if(f==0){
                    System.out.println("wrong credentials");
                    System.out.println("press any key to continue");
                    input.nextLine();
                    continue;
                }
                else{
                    user=true;
                    query="update instructor set token="+token+" where id='"+user_id+"';";
                    stmt.executeUpdate(query);
                    System.out.println("logged in successfully");
                    try {

                        // Open given file in append mode by creating an
                        // object of BufferedWriter class
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String time= dtf.format(now);
                        BufferedWriter out = new BufferedWriter(
                                new FileWriter("log.txt", true));
                        query="instructor "+user_id+" logged in on "+ time+"\n";
                        // Writing on output stream
                        out.write(query);
                        // Closing the connection
                        out.close();
                    }

                    // Catch block to handle the exceptions
                    catch (IOException e) {

                        // Display message when exception occurs
                        System.out.println("exception occurred" + e);
                    }
                    return;

                }


            } catch (SQLException e) {
                System.out.println(e);
                System.out.println("wrong credentials");
                System.out.println("press any key to continue");
                input.nextLine();
            }

        }
    }
    public static void logout(){
        user=false;
        String query="update instructor set token='logged out' where id='"+user_id+"';";
        try {

            stmt= conn.createStatement();
            stmt.executeUpdate(query);
            try {

                // Open given file in append mode by creating an
                // object of BufferedWriter class
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time= dtf.format(now);
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("log.txt", true));
                query="instructor "+user_id+" logged out on "+ time +"\n";
                // Writing on output stream
                out.write(query);
                // Closing the connection
                out.close();
            }

            // Catch block to handle the exceptions
            catch (IOException e) {

                // Display message when exception occurs
                System.out.println("exception occurred" + e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void viewprofile()
    {
        if(!user){
            System.out.println("please log in!");
            System.out.println("press any key to continue");
            input.nextLine();
            return;
        }

        String query="select * from instructor where id='"+user_id+"';";

        try {
            stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            ResultSetMetaData rsmd;
            rsmd=rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            String responseQuery="";
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {

                    if (i == 1)
                        responseQuery += "id ---> ";
                    if (i == 2)
                        responseQuery += "      name ---> ";
                    if (i == 3)
                        responseQuery += "      email ---> ";
                    if (i == 4)
                        responseQuery += "      dep_id ---> ";

                    if (i == 6)
                        responseQuery += "      phone_number ---> ";

                    if (i > 1 && i!=5 && i!=7)
                        responseQuery = responseQuery + " ";
                    if(i!=5 && i!=7) {
                        String columnValue = rs.getString(i);
                        responseQuery += columnValue;

                    }
                    // System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }

            }
            System.out.println(responseQuery);
            System.out.println("press any key to continue");
            input.nextLine();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateprofile(){
        if(!user){
            System.out.println("please log in!");
            System.out.println("press any key to continue");
            input.nextLine();
            return;
        }
         String name="",password="",phone_number;
        System.out.println("enter name to update");
        name=input.nextLine();
        System.out.println("enter phone number to update");
        phone_number=input.nextLine();
        System.out.println("enter password to update");
        password=input.nextLine();

        String query =" update instructor set name='"+name+"',phone_number='"+phone_number+"',password='"+password+"' where id='"+user_id+"';";

        try {
            stmt=conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("profile updated successfully");
            System.out.println("press any key to continue");
            input.nextLine();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static void addCourse(){
        if(!user){
            System.out.println("please log in!");
            System.out.println("press any key to continue");
            input.nextLine();
            return;
        }

        while(true){
            String course_id;
            System.out.println("enter the course_id or 0 to exit");
            course_id=input.nextLine();
if(course_id.equals("0")){
    return;
}
            try {
                stmt= conn.createStatement();
                String query="select * from course_catalog where course_id='"+course_id+"';";

                try {
                    ResultSet rs;


                    rs=stmt.executeQuery(query);


                }
                catch (SQLException e){
                    System.out.println("no such course in the course catalog");
                }

                String query2="select * from course_offering where course_id='"+course_id+"';";
                try {
                    ResultSet rs;
                    rs=stmt.executeQuery(query2);
                    String f="";
                    while(rs.next()) {
                       f = rs.getString(1);
                    }
                    if(f.equals(""))
                    {
                        String cgpa_limit;
                        System.out.println("set the cgpa limit for this course");
                        cgpa_limit=input.nextLine();

                        query="insert into course_offering(course_id,cgpa_limit,instructor_id) values ('"+course_id+"',"+cgpa_limit+",'"+user_id+"');";
                        stmt.executeUpdate(query);
                        System.out.println("Added course successfully");
                    }
                    else{
                        System.out.println("Course already offered by someone else");
                    }
                }
                catch (SQLException e){
                    System.out.println(e);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


    }

    public static void offeredCourses(){
        if(!user){
            System.out.println("please log in!");
            System.out.println("press any key to continue");
            input.nextLine();
            return;
        }

        String query="select * from course_catalog;";

        try {
            stmt= conn.createStatement();
            try {
                ResultSet rs=stmt.executeQuery(query);
                ResultSetMetaData rsmd;
                rsmd=rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                String responseQuery="";
                while (rs.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {

                        if (i == 1)
                            responseQuery += "course_id ---> ";
                            String columnValue = rs.getString(i);
                            responseQuery += columnValue;
                        // System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    }
responseQuery+="\n";
                }
                System.out.println(responseQuery);
                System.out.println("press any key to continue");
                input.nextLine();
            }
            catch (SQLException ee){
                System.out.println("semester has not started yet");
                System.out.println("press any key to continue");
                input.nextLine();

                return;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
public static void mycourses(){

        String query="select * from course_offering where instructor_id='"+user_id+"';";

    try {
        stmt= conn.createStatement();
        ResultSet rs=stmt.executeQuery(query);
        ResultSetMetaData rsmd;
        rsmd=rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        String responseQuery="";
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {

                if (i == 1)
                    responseQuery += "course_id ---> ";
                if (i == 2)
                    responseQuery += "cgpa_limit ---> ";
                if (i == 3)
                    responseQuery += "instructor_id ---> ";

                String columnValue = rs.getString(i);
                responseQuery += columnValue+" ";
                // System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            responseQuery+="\n";
        }
        System.out.println(responseQuery);
        System.out.println("press any key to continue");
        input.nextLine();
    } catch (SQLException e) {
        System.out.println("you have no offered courses");
    }
}
    public static void deleteCourse()
    {
        if(!user){
            System.out.println("please log in!");
            System.out.println("press any key to continue");
            input.nextLine();
            return;
        }

        while(true){
            String course_id;
            System.out.println("enter course_id to delete or 0 to exit");
            course_id=input.nextLine();
            if(course_id.equals("0")){
                return;
            }
            String query="delete from course_offering where course_id='"+course_id+"' and instructor_id='"+user_id+"';";
            try {
                stmt=conn.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println("you have not offered this course yet");
//                throw new RuntimeException(e);
            }
        }


    }


    public static void showGrades()
    {
        if(!user){
            System.out.println("please log in!");
            System.out.println("press any key to continue");
            input.nextLine();
            return;
        }

        try {
            stmt=conn.createStatement();
            String query = "select * from grades;";
            try {
                ResultSet rs;
                ResultSetMetaData rsmd;
                rs= stmt.executeQuery(query);
                rsmd=rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                String responseQuery="";
                while (rs.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {

                        if (i == 1)
                            responseQuery += "student_id ---> ";
                        if (i == 2)
                            responseQuery += "      course_id ---> ";
                        if (i == 3)
                            responseQuery += "      Grade ---> ";
                        if (i == 4)
                            responseQuery += "      semester ---> ";
                        if (i == 5)
                            responseQuery += "      academic year ---> ";

                        if (i > 1)
                            responseQuery = responseQuery + " ";
                        String columnValue = rs.getString(i);
                        // System.out.print(columnValue + " " + rsmd.getColumnName(i));
                        responseQuery += columnValue;
                    }
                    responseQuery = responseQuery + "\n";

                }
                System.out.println(responseQuery);
                System.out.println("press any key to continue");
                input.nextLine();
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void enrollmentRequests()
    {
        if(!user){
            System.out.println("please log in!");
            System.out.println("press any key to continue");
            input.nextLine();
            return;
        }
String query="select * from registration_status where instructor_id='"+user_id+"' and status='pending instructor approval';";

        try {
            stmt= conn.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            ResultSetMetaData rsmd=rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            String responseQuery="";
            int f=0;
            while (rs.next()) {
                f++;
                for (int i = 1; i <= columnsNumber; i++) {

                    if (i == 1)
                        responseQuery += "course_id ---> ";
                    if (i == 2)
                        responseQuery += "      student_id ---> ";


                    if (i > 1)
                        responseQuery = responseQuery + " ";
                    String columnValue = rs.getString(i);
                    // System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    responseQuery += columnValue;
                }
                responseQuery = responseQuery + "\n";

            }
            if(f==0){
                System.out.println("No enrolllments requests yet");
                System.out.println("press any key to continue");
                input.nextLine();
                return;
            }
            System.out.println(responseQuery);
            System.out.println("press any key to continue");
            input.nextLine();

            System.out.println("Approve or disapprove requests ");
            while(true){
                String course_id,student_id;
                System.out.println("enter course_id or 0 to exit");
                course_id=input.nextLine();
                if(course_id.equals("0")){
                    return;
                }
                System.out.println("enter Student_id ");
                student_id=input.nextLine();
                String resp;
                System.out.println("press 1 to approve and 2 to disapprove");
                resp=input.nextLine();
                if(resp.equals("1")){
                     query="update registration_status set status='approved by the instructor' where course_id='"+course_id+"' and student_id='"+student_id+"';";
                    stmt.executeUpdate(query);
                }
                else{
                    query="update registration_status set status='rejected by the instructor' where course_id='"+course_id+"' and student_id='"+student_id+"';";
                    stmt.executeUpdate(query);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void submitgrades(){
        String csvFilePath="src/main/resources/grades.csv";

        String cd="";
        String sql = "INSERT INTO grades (student_id,instructor_id, course_id, grade, semester, academic_year) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        BufferedReader lineReader = null;
        try {
            lineReader = new BufferedReader(new FileReader(csvFilePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String lineText = null;

        int count = 0;

        try {
            lineReader.readLine(); // skip header line
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                if (!((lineText = lineReader.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] data = lineText.split(",");
            if(data.length!=5){
                 System.out.println("Some lines were buggy");
                 continue;
            }
            String student_id = data[0];
            String course_id = data[1];

            cd=course_id;
            String grade = data[2];
            String semester = data[3];
            String academic_year = data[4] ;

            try{

                statement.setString(1, student_id);
                statement.setString(2, user_id);

                statement.setString(3, course_id);

                statement.setString(4, grade);

                statement.setString(5, semester);

                statement.setString(6, academic_year);
            }
            catch (Exception e){
                System.out.println(e);
            }

            try {
                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
count++;
        }
        if(count==0){
            System.out.println("plese enter some data in the file");
            System.out.println("press any key to continue");
            input.nextLine();
            return;
        }

        try {
            lineReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String query="select * from registration_status where course_id='"+cd+"';";
        try {
            stmt= conn.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            while(rs.next()){
                String sid=rs.getString(2);
                query="select * from grades where student_id='"+sid+"' and course_id='"+cd+"';";
//                System.out.println(query);
                ResultSet rs1=stmt.executeQuery(query);
                int f=0;
                while(rs1.next())f++;
                if(f==0){
                    System.out.println("no grade has been submitted for student with id "+sid);
                    query="delete from grades where instructor_id='"+user_id+"' and course_id='"+cd+"';";
                    stmt.executeUpdate(query);
                } else if (f>1) {
                    System.out.println("more than 1  grade has been submitted for student with id "+sid);
                    query="delete from grades where instructor_id='"+user_id+"' and course_id='"+cd+"';";
                    stmt.executeUpdate(query);

                }
            }
        } catch (SQLException e) {
//            throw new RuntimeException(e);
        }


        System.out.println("grades submitted successfully");
        System.out.println("press any key to continue");
        input.nextLine();
        // execute the remaining queries

    }




}
