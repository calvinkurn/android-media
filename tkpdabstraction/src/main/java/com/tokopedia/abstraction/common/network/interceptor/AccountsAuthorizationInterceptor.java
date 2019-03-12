package com.tokopedia.abstraction.common.network.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by nisie on 2/28/18.
 */

public class AccountsAuthorizationInterceptor implements Interceptor {

    private static final String KEY_ACCOUNTS_AUTHORIZATION = "Accounts-Authorization";
    private static final String BEARER = "Bearer";
    private final UserSessionInterface userSession;

    public AccountsAuthorizationInterceptor(@ApplicationContext Context context) {
        userSession = new UserSession(context);
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
        return newRequest;
    }
}
