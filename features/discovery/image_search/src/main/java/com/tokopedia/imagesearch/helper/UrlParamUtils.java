package com.tokopedia.imagesearch.helper;

import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class UrlParamUtils {

    public static HashMap<String, String> getParamMap(String paramString) {
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(paramString)) {
            String[] params = paramString.split("&");
            for (String param : params) {
                String[] val = param.split("=");
                if (val.length == 2) {
                    String name = val[0];
                    String value = val[1];
                    map.put(name, omitNewlineAndPlusSign(value));
                }
            }
        }
        return map;
    }

    private static String omitNewlineAndPlusSign(String text) {
        return text.replace("\n", "").replace("+", " ");
    }

    public static <T> String generateUrlParamString(Map<String, T> paramMap) {
        if (mapIsEmpty(paramMap)) {
            return "";
        }

        List<String> paramList = createParameterListFromMap(paramMap);

        return joinWithDelimiter("&", paramList);
    }

    private static <T> boolean mapIsEmpty(Map<String, T> paramMap) {
        return paramMap == null || paramMap.size() <= 0;
    }

    private static <T> List<String> createParameterListFromMap(Map<String, T> paramMap) {
        List<String> paramList = new ArrayList<>();

        for (Map.Entry<String, T> entry : paramMap.entrySet()) {
            if (mapEntryHasNulls(entry)) continue;

            addParameterEntryToList(paramList, entry);
        }

        return paramList;
    }

    private static <T> boolean mapEntryHasNulls(Map.Entry<String, T> entry) {
        return entry.getKey() == null || entry.getValue() == null;
    }

    private static <T> void addParameterEntryToList(List<String> paramList, Map.Entry<String, T> entry) {
        try {
            paramList.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String joinWithDelimiter(@NotNull CharSequence delimiter, @NotNull Iterable tokens) {
        final Iterator<?> it = tokens.iterator();

        if (!it.hasNext()) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();
        sb.append(it.next());

        while (it.hasNext()) {
            sb.append(delimiter);
            sb.append(it.next());
        }

        return sb.toString();
    }
}
