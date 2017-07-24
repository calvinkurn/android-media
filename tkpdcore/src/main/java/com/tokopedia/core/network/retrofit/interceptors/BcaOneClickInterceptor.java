package com.tokopedia.core.network.retrofit.interceptors;

import android.support.v4.util.ArrayMap;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.util.Map;

/**
 * Created by kris on 7/23/17. Tokopedia
 */

public class BcaOneClickInterceptor extends TkpdAuthInterceptor {

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> headerMap = new ArrayMap<>();
        headerMap.put("Authorization", "Basic ZmFkZjBkY2YtYjc1Zi00MTI1LWI1ZWItNzQxYWUxMjM0ZWFiOmM4NjUzMTJhLTZkNWYtNGVmNi05MzJmLWFmYWY4ZjhiZGVhNA==");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        return headerMap;
    }
}
