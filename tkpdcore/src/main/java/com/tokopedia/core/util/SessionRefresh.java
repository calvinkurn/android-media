package com.tokopedia.core.util;

import android.content.Context;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.accounts.apis.AccountsApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.session.presenter.Login;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * @author ricoharisin .
 */

public class SessionRefresh {

    public String refreshLogin() throws IOException {
        Context context = MainApplication.getAppContext();
        SessionHandler sessionHandler = new SessionHandler(context);
        String authKey = sessionHandler.getTokenType(context)
                + " " + sessionHandler.getAccessToken(context);
        RequestParams params = RequestParams.create();
        params.putString(Login.UUID_KEY, sessionHandler.getUUID());
        params.putString(Login.USER_ID, sessionHandler.getLoginID());
        Call<String> responseCall = getRetrofit(authKey)
                .create(AccountsApi.class).makeLoginsynchronous(
                        AuthUtil.generateParamsNetwork2(
                                MainApplication.getAppContext(), params.getParameters()));
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
