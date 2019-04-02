package com.tokopedia.core.base.utils;

import android.text.TextUtils;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author kulomady on 12/9/16.
 */

@Deprecated
public class StringUtils {

    public static String convertListToStringDelimiter(List<String> list, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            builder.append(it.next());
            if (it.hasNext()) {
                builder.append(delimiter);
            }
        }
        return builder.toString();
    }

    public static boolean isNotBlank(String shareUrl) {
        return !isBlank(shareUrl);
    }

    public static boolean isBlank(String shareUrl) {
        return shareUrl == null || shareUrl.length() == 0;
    }

    public static boolean containInList(List<String> stringList, String stringToCheck){
        if (stringList == null || TextUtils.isEmpty(stringToCheck)) {
            return false;
        }
        for (int i=0, sizei = stringList.size(); i<sizei; i++) {
            if (stringToCheck.equals(stringList.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static String omitPunctuationAndDoubleSpace(String stringToReplace){
        if (TextUtils.isEmpty(stringToReplace)){
            return "";
        }
        else {
            return stringToReplace.replaceAll("\\r|\\n", " ")
                    .replaceAll("\\s+", " ")
                    .replaceAll("[^0-9a-zA-Z ]", "")
                    .trim();
        }
    }

    public static boolean containNonSpaceAlphaNumeric(String stringToCheck){
        if (TextUtils.isEmpty(stringToCheck)){
            return false;
        }
        else {
            Pattern p = Pattern.compile("[^a-zA-Z 0-9]");
            return p.matcher(stringToCheck).find();
        }
    }
}
