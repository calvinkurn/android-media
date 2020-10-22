package com.tokopedia.core.util;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.network.apiservices.accounts.apis.AccountsBasicApi;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.session.model.TokenModel;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.refreshtoken.AccountsBasicInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.url.TokopediaUrl;
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
 * @author steven .
 */

@Deprecated
public class AccessTokenRefresh {

    private static final String FORCE_LOGOUT = "forced_logout";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String GRANT_TYPE = "grant_type";
    private static final String REFRESH_TOKEN = "refresh_token";

    public String refreshToken() throws IOException {
        Context context = CoreNetworkApplication.getAppContext();
        UserSession userSession = new UserSession(context);
        Map<String, String> params = new HashMap<>();

        params.put(GRANT_TYPE, REFRESH_TOKEN);
        params.put(ACCESS_TOKEN, userSession.getAccessToken());
        params.put(REFRESH_TOKEN, EncoderDecoder.Decrypt(userSession.getFreshToken(), userSession.getRefreshTokenIV()));

        Call<String> responseCall = getRetrofit(context, userSession, (NetworkRouter) context).create(AccountsBasicApi.class).getTokenSynchronous(params);

        String tokenResponse = null;
        String tokenResponseError = null;
        try {
            Response<String> response = responseCall.clone().execute();

            if (response.errorBody() != null) {
                tokenResponseError = response.errorBody().string();
                checkShowForceLogout(tokenResponseError);
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
            if(model.getAccessToken()!= null && !model.getAccessToken().trim().isEmpty()) {
                userSession.setToken(model.getAccessToken(), model.getTokenType());
            }

            if(model.getRefreshToken()!= null && !model.getRefreshToken().trim().isEmpty()) {
                userSession.setRefreshToken( EncoderDecoder.Encrypt(model.getRefreshToken(),
                        userSession.getRefreshTokenIV()));
            }
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
                .baseUrl(TokopediaUrl.Companion.getInstance().getACCOUNTS())
                .addConverterFactory(new StringResponseConverter())
                .client(tkpdOkHttpBuilder.addInterceptor(new AccountsBasicInterceptor(context, networkRouter, userSession))
                        .addInterceptor(new FingerprintInterceptor(networkRouter, userSession))
                        .addInterceptor(new AkamaiBotInterceptor(context))
                        .build())
                .build();
    }

    private Boolean isRequestDenied(String responseString) {
        return responseString.toLowerCase().contains(FORCE_LOGOUT);
    }

    private void checkShowForceLogout(String response) throws IOException {
        if (isRequestDenied(response)) {
            ServerErrorHandler.showForceLogoutDialog("");
            ServerErrorHandler.sendForceLogoutTokenAnalytics(response);
        }
    }
}
