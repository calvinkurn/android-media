package com.tokopedia.akamai_bot_lib.interceptor;

import com.tokopedia.akamai_bot_lib.UtilsKt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
//    static final Pattern p = Pattern.compile("(?<=mutation )(\\w*)(?=\\s*\\()" );

    public static boolean getMutation(String input, String match){
        Pattern p = Pattern.compile("(?<=mutation )(\\w*)(?=\\s*\\()" );

        String input2 = "mutation   login_email   \r\n\r\n($grant_type: String!, $username: String!, $password: String!, $supported:String!)";
        input2 = input2.replaceAll("\n", "");
        input2 =  input2.replaceAll("\\s+"," ");
        System.out.println("sebelum input2 "+input2+"\n input "+
                input);

        System.out.println("sesudah input2 "+input2+"\n input "+
                input.replaceAll("\n", "").replaceAll("\\s+", " "));


        System.out.println(input2.equalsIgnoreCase(
                input.replaceAll("\n", "").replaceAll("\\s+", " ")
        ));

        Matcher m = p.matcher(
                input.replaceAll("\n", "").replaceAll("\\s+", " ")
        );
        boolean returnVal = false;
        System.out.println(m.find());
        while (m.find()) {
            System.out.println("masuk sini");
            if( m.group(0).equalsIgnoreCase(match)) {
                System.out.println(m.group(0));
                returnVal = true;
                break;
            }
        }
        return returnVal;
    }
}
