package com.tokopedia.filter.newdynamicfilter.adapter.viewholder.widget;

import android.text.TextUtils;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by User on 11/10/2017.
 */

public class StringUtils {

    public static boolean isNotBlank(String shareUrl) {
        return !isBlank(shareUrl);
    }

    public static boolean isBlank(String shareUrl) {
        return shareUrl == null || shareUrl.length() == 0;
    }

    public static String omitNonNumeric(String stringToReplace){
        if (TextUtils.isEmpty(stringToReplace)){
            return "0";
        }
        else {
            return stringToReplace.replaceAll("[^\\d]", "");
        }
    }

    public static String removeComma(String numericString){
        return numericString.replace(",", "");
    }
}