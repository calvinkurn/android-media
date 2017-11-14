package com.tokopedia.core.util;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.tokocash.apis.TokoCashApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.entity.tokocash.WalletTokenEntity;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.WalletAuthInterceptor;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by nabillasabbaha on 10/12/17.
 */

public class WalletTokenRefresh {

    public String refreshToken() throws IOException {
        Context context = MainApplication.getAppContext();

        SessionHandler sessionHandler = new SessionHandler(context);

        Call<String> responseCall = getRetrofit(WalletAuthInterceptor.BEARER + " " + sessionHandler.getAccessToken())
                .create(TokoCashApi.class).getTokenWalletSynchronous();

        String tokenResponse = null;
        try {
            tokenResponse = responseCall.clone().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WalletTokenEntity tokenEntity = null;
        if (tokenResponse != null) {
            tokenEntity = new GsonBuilder().create().fromJson(tokenResponse, WalletTokenEntity.class);
            sessionHandler.setTokenTokoCash(tokenEntity.getToken());
        }
        return tokenEntity.getToken();
    }

    private Retrofit getRetrofit(String oauthAuthorization) {
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.ACCOUNTS_DOMAIN)
                .addConverterFactory(new StringResponseConverter())
                .client(OkHttpFactory.create().buildClientBearerAuth(oauthAuthorization))
                .build();
    }
}