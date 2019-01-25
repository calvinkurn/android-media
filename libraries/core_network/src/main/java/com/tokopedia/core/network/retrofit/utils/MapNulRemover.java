package com.tokopedia.core.network.retrofit.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author anggaprasetiyo on 10/3/16.
 */

@Deprecated
public class MapNulRemover {

    /**
     * @param paramOrigin map asli yang mungkin ada value null
     * @return map yang udah bersih
     */
    public static Map<String, String> removeNull(Map<String, String> paramOrigin) {
        Map<String, String> paramNull = new HashMap<>();
        for (Map.Entry<String, String> entry : paramOrigin.entrySet()) {
            if (entry.getValue() == null) {
                paramNull.put(entry.getKey(), "");
            }
        }
        paramOrigin.putAll(paramNull);
        return paramOrigin;
    }
}
