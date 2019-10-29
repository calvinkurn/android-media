package com.tokopedia.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceUtil {
    public static String from(String price){
        Pattern p = Pattern.compile("^\\d+");
        Matcher m = p.matcher(price);
        while(m.find()) {
            return m.group();
        }

        return "0";
    }
}
