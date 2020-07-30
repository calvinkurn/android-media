package com.tokopedia.loginregister.registerinitial.view.util;


/**
 * Created by nisie on 1/27/17.
 */

public class RegisterUtil {
    private static final int MAX_PHONE_NUMBER = 13;
    private static final int MIN_PHONE_NUMBER = 6;
    private static final int MAX_NAME = 35;
    private static final int MIN_NAME = 3;

    public static boolean checkRegexNameLocal(String param){
        String regex = "[A-Za-z]+";
        return !param.replaceAll("\\s","").matches(regex);
    }
    public static boolean isExceedMaxCharacter(String text) {
        return text.length() > MAX_NAME;
    }

    public static boolean isBelowMinChar(String text) {
        return text.length() < MIN_NAME;
    }

    public static boolean isValidPhoneNumber(String phoneNo) {
        for (int i = MIN_PHONE_NUMBER; i <= MAX_PHONE_NUMBER; i++) {
            if (phoneNo.matches("\\d{" + i + "}")) return true;
        }
        return false;

    }
}
