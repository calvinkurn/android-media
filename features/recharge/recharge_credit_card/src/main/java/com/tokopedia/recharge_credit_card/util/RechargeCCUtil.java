package com.tokopedia.recharge_credit_card.util;

import android.text.Editable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RechargeCCUtil {

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

    //this method used to separate cc per 4digits
    public static String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
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
        return (counter % 10 == 0);
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
