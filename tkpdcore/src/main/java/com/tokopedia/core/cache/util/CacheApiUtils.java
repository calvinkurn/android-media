package com.tokopedia.core.cache.util;

import android.text.TextUtils;

import com.tokopedia.core.cache.constant.HTTPMethodDef;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by nathan on 9/26/17.
 */

public class CacheApiUtils {

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
}
