package com.tokopedia.core.network.retrofit.interceptors;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * @author stevenfredian on 5/25/16.
 */
public class AccountsInterceptor extends TkpdAuthInterceptor {
    private static final String X_TKPD_PATH = "x-tkpd-path";
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    private static final String TAG = AccountsInterceptor.class.getSimpleName();
    private final String authKey;
    private final boolean isUsingHMAC;
    private final boolean isUsingBothAuthorization;

    public AccountsInterceptor(String key, boolean isUsingHMAC,
                               boolean isUsingBothAuthorization) {
        this.authKey = key;
        this.isUsingHMAC = isUsingHMAC;
        this.isUsingBothAuthorization = isUsingBothAuthorization;
    }

    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        if (isUsingBothAuthorization) {
            generateBothAuthRequest(originRequest, newRequest);
        } else if (isUsingHMAC) {
            generateHmacRequest(originRequest, newRequest);
        } else {
            generateAuthRequest(originRequest, newRequest);
        }
    }


    private void generateBothAuthRequest(Request originRequest, Request.Builder newRequest) {
        Map<String, String> authHeaders = AuthUtil.generateBothAuthHeadersAccount(
                originRequest.url().uri().getPath(),
                generateParamBodyString(originRequest),
                originRequest.method(),
                CONTENT_TYPE,
                getToken(),
                DATE_FORMAT);
        authHeaders.put(X_TKPD_PATH, originRequest.url().uri().getPath());
        generateHeader(authHeaders, originRequest, newRequest);
    }

    private void generateHmacRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders = prepareHeader(authHeaders, originRequest);
        authHeaders.put(X_TKPD_PATH, originRequest.url().uri().getPath());
        generateHeader(authHeaders, originRequest, newRequest);
    }

    private void generateAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        Map<String, String> authHeaders = AuthUtil.generateHeadersAccount(getToken());
        authHeaders.put(X_TKPD_PATH, originRequest.url().uri().getPath());
        generateHeader(authHeaders, originRequest, newRequest);
    }


    protected String getToken() {
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        if (!TextUtils.isEmpty(userSession.getAccessToken())) {
            SharedPreferences sharedPrefs = CoreNetworkApplication.getAppContext().getSharedPreferences("LOGIN_SESSION", Context.MODE_PRIVATE);
            String tokenType = sharedPrefs.getString("TOKEN_TYPE", "");
            return tokenType + " " + userSession.getAccessToken();
        } else {
            return authKey;
        }
    }

}
