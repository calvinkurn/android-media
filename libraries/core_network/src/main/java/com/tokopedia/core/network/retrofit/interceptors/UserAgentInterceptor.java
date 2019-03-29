package com.tokopedia.core.network.retrofit.interceptors;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.Request;

/**
 * Created by meta on 25/05/18.
 */

@Deprecated
public class UserAgentInterceptor extends TkpdAuthInterceptor {

    public UserAgentInterceptor() {}

    private final String userAgent = System.getProperty("http.agent");

    @Override
    protected Map<String, String> getHeaderMapNew(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> header = super.getHeaderMapNew(path, strParam, method, authKey, contentTypeHeader);;
        header.put("User-Agent", userAgent);
        return header;
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> header = super.getHeaderMap(path, strParam, method, authKey, contentTypeHeader);
        header.put("User-Agent", userAgent);
        return header;
    }
}