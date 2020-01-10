package com.tokopedia.tkpd.tkpdreputation.utils;

import android.content.Context;
import android.util.TypedValue;

import java.util.HashMap;
import java.util.Map;

public class ReputationUtil {

    public static HashMap<String, String> convertMapObjectToString(HashMap<String,Object> map) {
        HashMap<String,String> newMap =new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            newMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return newMap;
    }

    public static float DptoPx(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}