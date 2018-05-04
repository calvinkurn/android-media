package com.tokopedia.network.utils;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * @author ricoharisin .
 */

public class AccessTokenRefresh {
    public String refreshToken() throws IOException {
        Context context = MainApplication.getAppContext();

        SessionHandler sessionHandler = new SessionHandler(context);
        sessionHandler.clearToken();
        Map<String, String> params = new HashMap<>();

        params.put("grant_type", "refresh_token");
        params.put("refresh_token", EncoderDecoder.Decrypt(SessionHandler.getRefreshToken(context), SessionHandler.getRefreshTokenIV(context)));

        Call<String> responseCall = getRetrofit().create(AccountsBasicApi.class).getTokenSynchronous(params);

        String tokenResponse = null;
        try {
            tokenResponse = responseCall.clone().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TokenModel model = null;
        if (tokenResponse != null) {
            model = new GsonBuilder().create().fromJson(tokenResponse, TokenModel.class);
            sessionHandler.setToken(model.getAccessToken(), model.getTokenType());
        }

        return model.getAccessToken();
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.ACCOUNTS_DOMAIN)
                .addConverterFactory(new StringResponseConverter())
                .client(OkHttpFactory.create().buildBasicAuth())
                .build();
    }
}
