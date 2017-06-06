package com.tokopedia.core.util;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.accounts.apis.AccountsApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.session.presenter.Login;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author ricoharisin .
 */

public class SessionRefresh {

    public String refreshLogin() throws IOException {
        Bundle bundle = new Bundle();
        Context context = MainApplication.getAppContext();
        SessionHandler sessionHandler = new SessionHandler(context);
        String authKey;
        authKey = sessionHandler.getTokenType(context) + " " + sessionHandler.getAccessToken(context);
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        bundle.putString(AccountsService.WEB_SERVICE, AccountsService.WS);
        RequestParams params = RequestParams.create();
        params.putString(Login.UUID_KEY, sessionHandler.getUUID());
        params.putString(Login.USER_ID, sessionHandler.getLoginID());
        Call<String> responseCall = getRetrofit(authKey).create(AccountsApi.class).makeLoginsynchronous(params.getParameters());
        return responseCall.execute().body();
    }

    private Retrofit getRetrofit(String authKey) {
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.BASE_DOMAIN)
                .addConverterFactory(new StringResponseConverter())
                .client(OkHttpFactory.create().buildClientAccountsAuth(authKey, false, false))
                .build();
    }

}
