package com.tokopedia.topads.sdk.utils;

import android.text.TextUtils;

import com.tokopedia.topads.sdk.domain.TopAdsParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author errysuprayogi on 06,December,2018
 */
public class Utils {

    public static String encodeUrlParams(Map<String, String> paramSet){
        List<String> paramList = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramSet.entrySet()) {
            paramList.add(entry.getKey() + "=" + entry.getValue().replace(" ", "+"));
        }
        return TextUtils.join("&", paramList);
    }
}
