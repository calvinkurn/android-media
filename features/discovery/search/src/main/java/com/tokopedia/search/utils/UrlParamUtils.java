package com.tokopedia.search.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class UrlParamUtils {

    public static <T> String generateUrlParamString(Map<String, T> paramMap) {
        if (paramMap == null) {
            return "";
        }

        List<String> paramList = new ArrayList<>();

        for (Map.Entry<String, T> entry : paramMap.entrySet()) {
            if(entry.getValue() == null) continue;

            addParamToList(paramList, entry);
        }

        return TextUtils.join("&", paramList);
    }

    private static <T> void addParamToList(List<String> paramList, Map.Entry<String, T> entry) {
        try {
            paramList.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
