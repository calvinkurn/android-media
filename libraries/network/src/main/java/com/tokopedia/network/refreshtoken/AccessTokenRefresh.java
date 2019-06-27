package com.tokopedia.network.refreshtoken;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author ricoharisin .
 */

public class AccessTokenRefresh {

    private static final String FORCE_LOGOUT = "forced_logout";
    private static final String INVALID_REQUEST = "invalid_request";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String GRANT_TYPE = "grant_type";
    private static final String REFRESH_TOKEN = "refresh_token";

    public String refreshToken(Context context, UserSessionInterface userSession, NetworkRouter
            networkRouter) {

        Map<String, String> params = new HashMap<>();

        params.put(ACCESS_TOKEN, userSession.getAccessToken());
        params.put(GRANT_TYPE, REFRESH_TOKEN);
        params.put(REFRESH_TOKEN, EncoderDecoder.Decrypt(userSession.getFreshToken(), userSession.getRefreshTokenIV()));

        Call<String> responseCall = getRetrofit(context, userSession, networkRouter).create(AccountsBasicApi.class).getTokenSynchronous(params);

        String tokenResponse = null;
        String tokenResponseError = null;
        try {
            Response<String> response = responseCall.clone().execute();

            if (response.errorBody() != null) {
                tokenResponseError = response.errorBody().string();
                checkShowForceLogout(tokenResponseError, networkRouter);
            } else if (response.body() != null) {
                tokenResponse = response.body();
            } else {
                return "";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TokenModel model = null;
        if (tokenResponse != null) {
            model = new GsonBuilder().create().fromJson(tokenResponse, TokenModel.class);
            userSession.setToken(model.getAccessToken(), model.getTokenType());
        }

        if (model != null) {
            return model.getAccessToken();
        } else {
            return "";
        }
    }

    private Retrofit getRetrofit(Context context, UserSessionInterface userSession, NetworkRouter
            networkRouter) {
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder());
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.ACCOUNTS_DOMAIN)
                .addConverterFactory(new StringResponseConverter())
                .client(tkpdOkHttpBuilder.addInterceptor(new AccountsBasicInterceptor(context, networkRouter, userSession))
                        .addInterceptor(new FingerprintInterceptor(networkRouter, userSession))
                        .build())
                .build();
    }

    protected Boolean isRequestDenied(String responseString) {
        return responseString.toLowerCase().contains(FORCE_LOGOUT);
    }

    protected void checkShowForceLogout(String response, NetworkRouter networkRouter) {
        if (isRequestDenied(response)) {
            networkRouter.showForceLogoutTokenDialog(response);
        }
    }
}
