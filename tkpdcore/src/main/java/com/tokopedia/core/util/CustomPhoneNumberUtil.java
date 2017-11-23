package com.tokopedia.core.util;

import com.tokopedia.core.base.utils.StringUtils;

/**
 * Created by stevenfredian on 10/27/16.
 */

public class CustomPhoneNumberUtil {

    public static String transform(String phoneRawString){
        phoneRawString = checkStart(phoneRawString);
        phoneRawString = phoneRawString.replace("-","");
        StringBuilder phoneNumArr = new StringBuilder();
        for (int index = 0, limit = 4, size = phoneRawString.length();
             index < phoneRawString.length();
             index=index+limit) {
            if(size > limit+index){
                phoneNumArr.append(phoneRawString.substring(index,index+limit));
                phoneNumArr.append("-");
            }else{
                phoneNumArr.append(phoneRawString.substring(index,size));
            }
        }
        return phoneNumArr.toString();
    }

    private static String checkStart(String phoneRawString) {
        if(phoneRawString.startsWith("62")){
            phoneRawString = phoneRawString.replaceFirst("62","0");
        }else if(phoneRawString.startsWith("+62")) {
            phoneRawString = phoneRawString.replaceFirst("\\+62","0");
        }
        return phoneRawString;
    }
}
