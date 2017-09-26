package com.tokopedia.core.cache.util;

import android.text.TextUtils;

import com.tokopedia.core.cache.constant.HTTPMethodDef;

import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by nathan on 9/26/17.
 */

public class CacheApiUtils {

    private static final String HTTPS = "https://";
    private static final String COM_WITH_SLASH = ".com/";
    private static final String COM1 = ".com";

    public static String getFullRequestURL(Request request){
        String s = "";
        if (HTTPMethodDef.TYPE_POST.equalsIgnoreCase(request.method())) {
            RequestBody requestBody = request.body();
            if (requestBody instanceof FormBody) {
                int size = ((FormBody) requestBody).size();
                for (int i = 0; i < size; i++) {
                    String key = ((FormBody) requestBody).encodedName(i);
                    if (key.equals("hash") || key.equals("device_time")) {
                        continue;
                    }

                    String value = ((FormBody) requestBody).encodedValue(i);
                    if (TextUtils.isEmpty(s)) {
                        s += "?";
                    } else {
                        s += "&";
                    }
                    s += key + "=" + value;
                }
            }
        }
        return request.url().toString() + s;
    }

    public static String generateCacheHost(String host) {
        return (host.replace(HTTPS, "").replace(COM_WITH_SLASH, COM1));
    }

    public static String generateCachePath(String path) {
        if (!path.startsWith("/")) {
            return "/" + path;
        } else {
            return path;
        }
    }

    public static String getDomain(String fullPath) {
        try {
            URL url = new URL(fullPath);
            return  url.getAuthority();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPath(String fullPath) {
        try {
            URL url = new URL(fullPath);
            return  url.getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
