package com.example.akamai_bot_lib;

import com.tokopedia.akamai_bot_lib.UtilsKt;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testMutation(){

        String input = "mutation RegisterUsername ($affiliateName: String!) {\\r\\n  bymeRegisterAffiliateName(affiliateName: $affiliateName) {\\r\\n    success\\r\\n    error {\\r\\n      message\\r\\n      type\\r\\n      code\\r\\n    }\\r\\n  }\\r\\n}\\r\\n";

        assertTrue(UtilsKt.getMutation(input, "RegisterUsername"));

    }

    @Test
    public void staticTest(){
//        Pattern p = Pattern.compile("(?<=mutation )(\\w*)(?=\\s*\\()" );
//        String input = "mutation   login_email   \n\n($grant_type: String!, $username: String!, $password: String!, $supported:String!)";
//        input = input.replaceAll("\n", "");
//        input =  input.replaceAll("\\s+"," ");
////        System.out.println(input);
//
//        Matcher m = p.matcher(input);
//        while (m.find()) {
//            System.out.println(m.group(0));
//        }

        Pattern p = Pattern.compile("\\{.*?([a-zA-Z_][a-zA-Z0-9_]\\+)(?=\\().*}");
        String input = "mutation   login_email   \n\n($grant_type: String!, $username: String!, $password: String!, $supported:String!)";
        input = input.replaceAll("\n", "");
        input =  input.replaceAll("\\s+"," ");
//        System.out.println(input);

        Matcher m = p.matcher(input);
        while (m.find()) {
            System.out.println(m.group(0));
        }
    }


}