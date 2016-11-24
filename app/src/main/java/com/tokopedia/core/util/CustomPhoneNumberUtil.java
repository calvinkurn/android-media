package com.tokopedia.core.util;

import android.util.Log;

/**
 * Created by stevenfredian on 10/27/16.
 */

public class CustomPhoneNumberUtil {

    public static String transform(String phoneRawString) {
        phoneRawString = phoneRawString.replace("-", "");
        StringBuilder phoneNumArr = new StringBuilder();
        for (int index = 0, limit = 4, size = phoneRawString.length();
             index < phoneRawString.length();
             index = index + limit) {
            if (size > limit + index) {
                phoneNumArr.append(phoneRawString.substring(index, index + limit));
                phoneNumArr.append("-");
            } else {
                phoneNumArr.append(phoneRawString.substring(index, size));
            }
        }
        return phoneNumArr.toString();
    }
}
