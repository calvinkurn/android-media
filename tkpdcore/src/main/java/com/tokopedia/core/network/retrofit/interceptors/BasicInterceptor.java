package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;

/**
 * @author by nisie on 12/7/17.
 */

public class BasicInterceptor extends TkpdAuthInterceptor {

    private final String clientId;
    private final String clientSecret;

    public BasicInterceptor(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    protected void generateHmacAuthRequest(Request originRequest,
                                           Request.Builder newRequest) throws IOException {
        Map<String, String> authHeaders = AuthUtil.generateHeadersBasic(clientId, clientSecret);
        generateHeader(authHeaders, originRequest, newRequest);
    }
}
