package com.tokopedia.loginregister.registeremail.view.util;


import android.content.Context;

import com.tokopedia.loginregister.R;

/**
 * Created by nisie on 1/27/17.
 */

public class RegisterUtil {
    public static final int MAX_PHONE_NUMBER = 13;
    public static final int MIN_PHONE_NUMBER = 6;
    public static final int MAX_NAME = 35;
    public static final int MAX_NAME_128 = 128;
    public static final int MIN_NAME = 3;


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

    public static String formatDateText(int mDateDay, int mDateMonth, int mDateYear) {
        return String.format("%d / %d / %d", mDateDay, mDateMonth, mDateYear);
    }

    public static String formatDateTextString(Context context, int mDateDay, int mDateMonth, int 
            mDateYear) {
        String bulan;
        switch(mDateMonth) {
            case 1: bulan = context.getString(R.string.january); break;
            case 2: bulan = context.getString(R.string.february); break;
            case 3: bulan = context.getString(R.string.march); break;
            case 4: bulan = context.getString(R.string.april); break;
            case 5: bulan = context.getString(R.string.may); break;
            case 6: bulan = context.getString(R.string.june); break;
            case 7: bulan = context.getString(R.string.july); break;
            case 8: bulan = context.getString(R.string.august); break;
            case 9: bulan = context.getString(R.string.september); break;
            case 10: bulan = context.getString(R.string.october); break;
            case 11: bulan = context.getString(R.string.november); break;
            case 12: bulan = context.getString(R.string.december); break;
            default: bulan = context.getString(R.string.wrong_input); break;
        }
        return String.format("%d %s %d", mDateDay, bulan, mDateYear);
    }

    public static boolean isValidPhoneNumber(String phoneNo) {
        for (int i = MIN_PHONE_NUMBER; i <= MAX_PHONE_NUMBER; i++) {
            if (phoneNo.matches("\\d{" + i + "}")) return true;
        }
        return false;

    }
}
