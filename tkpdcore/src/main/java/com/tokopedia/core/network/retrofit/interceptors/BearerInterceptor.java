package com.tokopedia.core.network.retrofit.interceptors;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This interceptor is to handled
 */

public class BearerInterceptor implements Interceptor {
    private static final String HEADER_DEVICE = "X-Device";
    public static final String ANDROID = "android";
    private SessionHandler sessionHandler;

    public BearerInterceptor(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder reqBuilder = request.newBuilder();
        if (!TextUtils.isEmpty(sessionHandler.getLoginID())) {
            reqBuilder.header("Authorization", sessionHandler.getAccessToken());
        }
        reqBuilder.header(HEADER_DEVICE, ANDROID);
        Request req = reqBuilder.build();
        return chain.proceed(req);
    }
}
