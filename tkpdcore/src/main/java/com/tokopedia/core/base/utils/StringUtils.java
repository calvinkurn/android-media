package com.tokopedia.core.base.utils;

import java.util.Iterator;
import java.util.List;

/**
 * @author kulomady on 12/9/16.
 */

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
}
