package com.tokopedia.iris.util;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {
    public static Map<String, Object> bundleToMap(Bundle extras) {
        Map<String, Object> map = new HashMap<>();

        Set<String> ks = extras.keySet();
        for (String key : ks) {
            Object object = extras.get(key);
            if (object != null) {
                if (object instanceof ArrayList) {
                    object = convertAllBundleToMap((ArrayList) object);
                } else if (object instanceof Bundle) {
                    object = bundleToMap((Bundle) object);
                }
                map.put(key, object);
            }
        }
        return map;
    }

    public static List<Object> convertAllBundleToMap(ArrayList list) {
        List<Object> newList = new ArrayList<>();
        for (Object object : list) {
            if (object instanceof Bundle) {
                newList.add(bundleToMap((Bundle) object));
            } else {
                newList.add(object);
            }
        }
        return newList;
    }
}
