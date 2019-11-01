package com.tokopedia.core.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceUtil {
    public static String from(String price){
        if (!TextUtils.isEmpty(price)) {
            price = price.replaceAll("[^\\d]", "");
            return price;
        } else {
            return "0";
        }
    }
}
