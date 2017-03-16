package com.tokopedia.core.network.retrofit.interceptors;

import java.io.IOException;

import okhttp3.Request;

/**
 * Created by nakama on 16/03/17.
 */

public class EnrichedTkpdAuthInterceptor extends GlobalTkpdAuthInterceptor {
    private static final String HEADER_DEVICE_ID = "Tkpd-SessionId";
    private final String deviceId;

    public EnrichedTkpdAuthInterceptor(String key, String deviceId) {
        super(key);
        this.deviceId = deviceId;
    }

    @Override
    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest) throws IOException {
        newRequest.addHeader(HEADER_DEVICE_ID, deviceId);
        super.generateHmacAuthRequest(originRequest, newRequest);
    }
}
