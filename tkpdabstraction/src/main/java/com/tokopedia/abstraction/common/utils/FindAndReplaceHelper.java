package com.tokopedia.abstraction.common.utils;

import android.text.TextUtils;

public class FindAndReplaceHelper {

    //provide values in sequence replacementKey, replacementValue...
    //Make sure values to be replaced are not null by function caller
    public static String findAndReplacePlaceHolders(String soucrceString, String ...values){
        if(!TextUtils.isEmpty(soucrceString)) {
            StringBuilder stringBuilder = new StringBuilder(soucrceString);
            if (values != null && values.length >= 2) {
                for(int i = 0; i+1 < values.length; i+=2){
                    if(!TextUtils.isEmpty(values[i]) && values[i+1] != null) {
                        replace(values[i], values[i + 1], stringBuilder);
                    }
                }
            }
            return stringBuilder.toString();
        }
        else return soucrceString;
    }

    public static void replace( String target, String replacement,
                                StringBuilder builder ) {
        int indexOfTarget = -1;
        while( ( indexOfTarget = builder.indexOf( target ) ) >= 0 ) {
            builder.replace( indexOfTarget, indexOfTarget + target.length() , replacement );
        }
    }

}
