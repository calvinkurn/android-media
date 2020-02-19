package com.tokopedia.graphql.util;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.graphql.CommonUtils;
import com.tokopedia.graphql.data.model.BackendCache;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CacheHelper {

    public static final String SEP_CACHE = "\\|";
    public static final String SEP_CACHE_ITEM = ",";
    public static final String KEY_DIGEST_MD5 = "k";
    public static final String KEY_TYPE = "t";
    public static final String KEY_EXPIRY_TIME = "e";
    public static final String KEY_DEATH_TIME = "dt";
    public static final String KEY_IS_GUEST = "g";
    public static final String SAP_HASH_KEY = "-";
    public static final int LENGTH_MD5 = 32;
    public static final String REGEX_QUERY_NAME = "\\w+[(][$]|\\w+[(]|\\w+[{]|\\w+\\s+[(][$]|\\w+\\s+[(]|\\w+\\s[{]";
    public static final String REGEX_NO_SPECIAL_CHAR = "[^a-zA-Z0-9]+";


    public static Map<String, BackendCache> parseCacheHeaders(String cacheHeadersStr) {
        //k=fake-v3.5.4#f5f70ca40bd76513081a61a51242eb3b,t=0,e=100,dt=0,g=0
        if (TextUtils.isEmpty(cacheHeadersStr)) {
            return null;
        }

        String[] rules = cacheHeadersStr.split(SEP_CACHE);

        if (rules.length < 1) {
            return null;
        }

        Map<String, BackendCache> objCaches = new HashMap<>();
        for (String cacheItem : rules) {
            if (TextUtils.isEmpty(cacheItem)) {
                continue;
            }

            Map<String, String> items = getCachingParamMap(cacheItem.split(SEP_CACHE_ITEM));//String[] items = cacheItem.split(SEP_CACHE_ITEM);

            if (items == null
                    || items.isEmpty()
                    || TextUtils.isEmpty(items.get(KEY_DIGEST_MD5))) {
                continue;
            }

            int cType = parseInt(items.get(KEY_TYPE));

            if (cType == CACHE_TYPE.CACHE_FIRST.ordinal()
                    || cType == CACHE_TYPE.DO_CACHE.ordinal()) {
                if (TextUtils.isEmpty(items.get(KEY_DIGEST_MD5))) {
                    continue;
                }

                String[] keyHashPart = items.get(KEY_DIGEST_MD5).split("-");

                if (keyHashPart.length != 3) {
                    continue;
                }

                BackendCache backendCache = new BackendCache();
                backendCache.setDigest(keyHashPart[2]);
                backendCache.setCacheType(cType);
                backendCache.setMaxAge(parseInt(items.get(KEY_EXPIRY_TIME)));
                backendCache.setAutoPurgeTime(parseInt(items.get(KEY_DEATH_TIME)));
                backendCache.setGuest(parseInt(items.get(KEY_IS_GUEST)) == 1);

                objCaches.put(keyHashPart[2], backendCache);
            }
        }

        return objCaches;
    }

    private static int parseInt(String val) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public enum CACHE_TYPE {
        NO_CACHE,
        DO_CACHE,
        CACHE_FIRST
    }

    public static Object getResponse(JsonArray arrays, Type type) {
        //TODO lavekush-t please revisit for optimization
        for (JsonElement responseItem : arrays) {
            if (responseItem == null || responseItem.isJsonNull()) {
                continue;
            }

            Object object = CommonUtils.fromJson(responseItem.getAsJsonObject().toString(), type);
            if (object != null) {
                return object;
            }
        }

        return null;
    }

    public static String getQueryName(String query) {
        String oName = "not_supported";
        final Matcher m = Pattern.compile(REGEX_QUERY_NAME).matcher(query);

        final List<String> matches = new ArrayList<>();
        while (m.find()) {
            matches.add(m.group(0));
        }

        if (matches.isEmpty()) {
            return oName;
        }


        if ((matches.get(0) == null || matches.get(0).contains("query"))
                && matches.size() > 1) {
            oName = matches.get(1);
        } else {
            oName = matches.get(0);
        }

        oName = oName.replaceAll(REGEX_NO_SPECIAL_CHAR, "");

        return oName + "-v" + GlobalConfig.VERSION_NAME;
    }

    public static Map<String, String> getCachingParamMap(String[] items) {
        if (items == null || items.length < 1) {
            return null;
        }

        Map<String, String> paramMap = new HashMap<>();
        for (String param : items) {
            if (TextUtils.isEmpty(param)) {
                continue;
            }

            String[] keyVal = param.split("=");

            if (keyVal.length < 2) {
                continue;
            }

            if (TextUtils.isEmpty(keyVal[0]) || TextUtils.isEmpty(keyVal[1])) {

            }

            paramMap.put(keyVal[0], keyVal[1]);
        }

        return paramMap;
    }

}
