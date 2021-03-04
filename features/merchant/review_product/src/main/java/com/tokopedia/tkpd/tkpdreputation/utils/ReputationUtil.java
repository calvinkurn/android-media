package com.tokopedia.tkpd.tkpdreputation.utils;

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

}