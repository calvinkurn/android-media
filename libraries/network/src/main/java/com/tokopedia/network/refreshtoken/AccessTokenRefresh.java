package com.tokopedia.network.refreshtoken;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * @author ricoharisin .
 */

public class AccessTokenRefresh {

    public String refreshToken(Context context, UserSession userSession, NetworkRouter networkRouter)  {

        userSession.clearToken();
        Map<String, String> params = new HashMap<>();

        params.put("grant_type", "refresh_token");
        params.put("refresh_token", EncoderDecoder.Decrypt(userSession.getFreshToken(), userSession.getRefreshTokenIV()));

        Call<String> responseCall = getRetrofit(context, userSession, networkRouter).create(AccountsBasicApi.class).getTokenSynchronous(params);

        String tokenResponse = null;
        try {
            tokenResponse = responseCall.clone().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TokenModel model = null;
        if (tokenResponse != null) {
            model = new GsonBuilder().create().fromJson(tokenResponse, TokenModel.class);
            userSession.setToken(model.getAccessToken(), model.getTokenType());
        }

        return model.getAccessToken();
    }

    private Retrofit getRetrofit(Context context, UserSession userSession, NetworkRouter networkRouter) {
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.ACCOUNTS_DOMAIN)
                .addConverterFactory(new StringResponseConverter())
                .client(tkpdOkHttpBuilder.addInterceptor(
                        new AccountsBasicInterceptor(context, networkRouter, userSession))
                        .build())
                .build();
    }
}
