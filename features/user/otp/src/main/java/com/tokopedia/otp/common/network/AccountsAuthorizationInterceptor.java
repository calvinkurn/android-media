package com.tokopedia.otp.common.network;

import com.tokopedia.abstraction.common.data.model.session.UserSession;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by nisie on 4/26/18.
 */


public class AccountsAuthorizationInterceptor implements Interceptor {

    private static final String KEY_ACCOUNTS_AUTHORIZATION = "Accounts-Authorization";
    private static final String BEARER = "Bearer";
    private final UserSession userSession;

    public AccountsAuthorizationInterceptor(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest = addAccountsAuthorizationHeader(newRequest);

        return chain.proceed(newRequest.build());
    }

    private Request.Builder addAccountsAuthorizationHeader(Request.Builder newRequest) {
        newRequest.addHeader(KEY_ACCOUNTS_AUTHORIZATION, BEARER + " "
                + userSession.getAccessToken());
        return newRequest;
    }
}
