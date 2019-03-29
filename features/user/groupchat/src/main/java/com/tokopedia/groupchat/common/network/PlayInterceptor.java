package com.tokopedia.groupchat.common.network;


import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by nisie on 2/28/18.
 */

public class PlayInterceptor implements Interceptor {

    private static final String KEY_ACCOUNTS_AUTHORIZATION = "Accounts-Authorization";
    private static final String GC_TOKEN = "X-User-Token";

    private static final String BEARER = "Bearer";
    private final UserSessionInterface userSession;

    @Inject
    public PlayInterceptor(UserSessionInterface userSession) {
        this.userSession = userSession;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest = addAccountsAuthorizationHeader(newRequest);

        return chain.proceed(newRequest.build());
    }

    private Request.Builder addAccountsAuthorizationHeader(Request.Builder newRequest) {
        if (userSession.isLoggedIn()) {
            newRequest.addHeader(KEY_ACCOUNTS_AUTHORIZATION, BEARER + " "
                    + userSession.getAccessToken());
            newRequest.removeHeader("authorization");
        }

        newRequest.addHeader(GC_TOKEN, userSession.getGCToken());
        return newRequest;
    }
}
