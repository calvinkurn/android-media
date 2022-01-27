package com.tokopedia.recharge_credit_card.util;

import android.text.Editable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RechargeCCUtil {

    /*private static final int FIRST_AMEX_DIVIDER_POSITION = 4;
    private static final int SECOND_AMEX_DIVIDER_POSITION = 10;
    private static final int FIRST_AMEX_DIVIDER_POSITION_CHECKER = FIRST_AMEX_DIVIDER_POSITION + 1;
    private static final int SECOND_AMEX_DIVIDER_POSITION_CHECKER = SECOND_AMEX_DIVIDER_POSITION + 2;
    private static final int THIRD_AMEX_DIVIDER_POSITION_CHECKER = SECOND_AMEX_DIVIDER_POSITION_CHECKER + 5;*/
    private static final int DIVIDER_POSITION = 4;
    private static final int LUHN_ALGORITHM_MODULO = 10;

    public static boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
        boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
        for (int i = 0; i < s.length(); i++) { // check that every element is right
            if (i > 0 && (i + 1) % dividerModulo == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    /*public static boolean isInputCorrectAmex(Editable s, int totalSymbols, char divider) {
        boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
        for (int i = 0; i < s.length(); i++) { // check that every element is right
            if ((i + 1) == FIRST_AMEX_DIVIDER_POSITION_CHECKER
                    || (i + 1) == SECOND_AMEX_DIVIDER_POSITION_CHECKER
                    || (i + 1) == THIRD_AMEX_DIVIDER_POSITION_CHECKER) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }*/

    //this method used for separate cc with following patterns xxxx-xxxxxx-xxxxx
    /*public static String concatStringWith15D(char[] text, char divider) {
        StringBuilder formatted = new StringBuilder();
        int count = 0;
        for (char c : text) {
            if (Character.isDigit(c)) {
                if (count > 0
                        && ((count == FIRST_AMEX_DIVIDER_POSITION)
                        || (count == SECOND_AMEX_DIVIDER_POSITION))) {
                    formatted.append(divider);
                }
                formatted.append(c);
                ++count;
            }
        }
        return formatted.toString();
    }*/

    //this method used for separate cc with following patterns xxxx-xxxx-xxxx-xxxx
    public static String concatStringWith16D(char[] text, char divider){
        StringBuilder formatted = new StringBuilder();
        int count = 0;
        for (char c : text) {
            if (Character.isDigit(c)) {
                if (count % DIVIDER_POSITION == 0 && count > 0) {
                    formatted.append(divider);
                }
                formatted.append(c);
                ++count;
            }
        }
        return formatted.toString();
    }

    public static char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }

    //validation using check luhn algorithm
    public static boolean isCreditCardValid(String str) {
        int[] luhnArr = new int[]{0, 2, 4, 6, 8, 1, 3, 5, 7, 9};
        int counter = 0;
        int incNum;
        boolean odd = false;
        String temp = str.replace("/[^\\d]/g", "");
        if (temp.length() == 0)
            return false;
        for (int i = temp.length() - 1; i >= 0; --i) {
            incNum = Integer.parseInt(String.valueOf(temp.charAt(i)));
            counter += (odd = !odd) ? incNum : luhnArr[incNum];
        }
        return (counter % LUHN_ALGORITHM_MODULO == 0);
    }

    public static String generateIdemPotencyCheckout(String userLoginId) {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = md5(timeMillis);
        return userLoginId + "_" + (token.isEmpty() ? timeMillis : token);
    }

    private static String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b & 0xff));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
