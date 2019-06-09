package com.tokopedia.tkpd.network;

import android.support.annotation.NonNull;

import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by nisie on 10/16/18.
 * Puts Bearer Authorization in `Authorization` instead of `Accounts-Authorization`. Please do
 * not use this. This is only used for old class
 */
public class AccountsBearerInterceptor implements Interceptor {

    private static final String KEY_AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";
    private final UserSessionInterface userSession;

    public AccountsBearerInterceptor(UserSessionInterface userSession) {
        this.userSession = userSession;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest = replaceAuthorization(newRequest);
        return chain.proceed(newRequest.build());
    }

    private Request.Builder replaceAuthorization(Request.Builder newRequest) {
        newRequest.removeHeader(KEY_AUTHORIZATION);
        newRequest.addHeader(KEY_AUTHORIZATION, BEARER + " " + userSession.getAccessToken());
        return newRequest;
    }
}
