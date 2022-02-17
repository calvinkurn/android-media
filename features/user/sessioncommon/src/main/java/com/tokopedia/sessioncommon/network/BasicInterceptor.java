package com.tokopedia.sessioncommon.network;

import android.content.Context;
import android.util.Base64;

import com.tokopedia.network.authentication.AuthConstant;
import com.tokopedia.network.authentication.AuthHelper;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * @author by nisie on 10/16/18.
 */
public class BasicInterceptor extends TkpdOldAuthInterceptor {
    private static final String X_TKPD_PATH = "x-tkpd-path";
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_DEVICE = "X-Device";
    private static final String HEADER_X_APP_VERSION = "X-APP-VERSION";
    public static final String HEADER_X_TKPD_APP_NAME = "X-Tkpd-App-Name";
    private static final String HEADER_X_TKPD_APP_VERSION = "X-Tkpd-App-Version";
    private static final String HEADER_CACHE_CONTROL = "cache-control";

    public BasicInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession) {
        super(context, networkRouter, userSession);
    }

    public BasicInterceptor(Context context, NetworkRouter networkRouter, UserSessionInterface userSession) {
        super(context, networkRouter, userSession);
    }

    public BasicInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession, String authKey) {
        super(context, networkRouter, userSession, authKey);
    }

    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {

        generateAuthRequest(originRequest, newRequest);
    }

    private void generateAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        String clientID = "7ea919182ff";
        String clientSecret = "b36cbf904d14bbf90e7f25431595a364";
        String encodeString = clientID + ":" + clientSecret;

        String asB64 = Base64.encodeToString(encodeString.getBytes(), Base64.NO_WRAP);

        Map<String, String> authHeaders = new HashMap<>();
        authHeaders.put(HEADER_CONTENT_TYPE, CONTENT_TYPE);
        authHeaders.put(HEADER_CACHE_CONTROL, "no-cache");
        authHeaders.put(HEADER_AUTHORIZATION, "Basic " + asB64);
        authHeaders.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        authHeaders.put(AuthConstant.HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX);
        authHeaders.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        authHeaders.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        authHeaders.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);

        authHeaders.put(X_TKPD_PATH, originRequest.url().uri().getPath());
        generateHeader(authHeaders, originRequest, newRequest);
    }

    protected void generateHeader(
            Map<String, String> authHeaders, Request originRequest, Request.Builder newRequest) {
        for (Map.Entry<String, String> entry : authHeaders.entrySet())
            newRequest.addHeader(entry.getKey(), entry.getValue());
        newRequest.method(originRequest.method(), originRequest.body());
    }
}
