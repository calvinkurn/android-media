package com.tokopedia.core.util;

import android.text.TextUtils;


public class PriceUtil {
    public static String from(String price){
        price = removeCurrencyPrefix(price);
        if (!TextUtils.isEmpty(price)) {
            long result = Double.valueOf(price).longValue();
            return Long.valueOf(result).toString();
        } else {
            return "0";
        }
    }

    public static String removeCurrencyPrefix(String string){
        if (string == null) return null;
        int count = 0;
        for (int i=0, sizei = string.length();i<sizei ; i++) {
            char charString = string.charAt(i);
            if (Character.isDigit(charString)){
                break;
            }
            else {
                count++;
            }
        }
        return string.substring(count);
    }
}
