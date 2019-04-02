package com.tokopedia.otp.common.network;


import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by nisie on 4/27/18.
 */

public class AuthorizationBearerInterceptor implements Interceptor {

    private static final String KEY_AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";
    private final UserSessionInterface userSession;

    public AuthorizationBearerInterceptor(UserSessionInterface userSession) {
        this.userSession = userSession;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest = addAccountsAuthorizationHeader(newRequest);

        return chain.proceed(newRequest.build());
    }

    private Request.Builder addAccountsAuthorizationHeader(Request.Builder newRequest) {
        newRequest.removeHeader(KEY_AUTHORIZATION);
        newRequest.addHeader(KEY_AUTHORIZATION, BEARER + " "
                + userSession.getAccessToken());
        return newRequest;
    }
}