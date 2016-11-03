package com.tokopedia.tkpd.network.retrofit.interceptors;

import android.util.Log;

import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @author stevenfredian on 5/25/16.
 */
public class AccountsInterceptor extends TkpdAuthInterceptor {
    private static final String TAG = AccountsInterceptor.class.getSimpleName();
    private final String authKey;
    private final boolean isUsingHMAC;

    public AccountsInterceptor(String key, boolean isUsingHMAC) {
        this.authKey = key;
        this.isUsingHMAC = isUsingHMAC;
    }

    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        if (isUsingHMAC) {
            generateHmacRequest(originRequest, newRequest);
        } else {
            generateAuthRequest(originRequest, newRequest);
        }
    }

    private void generateHmacRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders = prepareHeader(authHeaders, originRequest);
        authHeaders.put("x-tkpd-path", originRequest.url().uri().getPath());
        generateHeader(authHeaders, originRequest , newRequest);
    }

    private void generateAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        Map<String, String> authHeaders = AuthUtil.generateHeadersAccount(authKey);
        authHeaders.put("x-tkpd-path", originRequest.url().uri().getPath());
        generateHeader(authHeaders, originRequest , newRequest);
    }
}
