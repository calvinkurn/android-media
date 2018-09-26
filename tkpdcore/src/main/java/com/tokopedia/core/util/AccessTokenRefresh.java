package com.tokopedia.core.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.accounts.apis.AccountsBasicApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.session.model.TokenModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author steven .
 */

public class AccessTokenRefresh {

    private static final String FORCE_LOGOUT = "forced_logout";
    private static final String INVALID_REQUEST = "invalid_request";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String GRANT_TYPE = "grant_type";
    private static final String REFRESH_TOKEN = "refresh_token";

    public String refreshToken() throws IOException {
        Context context = MainApplication.getAppContext();

        SessionHandler sessionHandler = new SessionHandler(context);
        Map<String, String> params = new HashMap<>();

        params.put(GRANT_TYPE, REFRESH_TOKEN);
        params.put(ACCESS_TOKEN, SessionHandler.getAccessToken());
        params.put(REFRESH_TOKEN, EncoderDecoder.Decrypt(SessionHandler.getRefreshToken(context), SessionHandler.getRefreshTokenIV(context)));

        Call<String> responseCall = getRetrofit().create(AccountsBasicApi.class).getTokenSynchronous(params);

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
            sessionHandler.setToken(model.getAccessToken(), model.getTokenType());
        }

        if (model != null) {
            return model.getAccessToken();
        } else {
            return "";
        }
    }

    private Retrofit getRetrofit() {
        Gson gson = new Gson();
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.ACCOUNTS_DOMAIN)
                .addConverterFactory(new StringResponseConverter())
                .client(OkHttpFactory.create().buildBasicAuth())
                .build();
    }

    protected Boolean isRequestDenied(String responseString) {
        return responseString.toLowerCase().contains(FORCE_LOGOUT);
    }

    protected void checkShowForceLogout(String response) throws IOException {
        if (isRequestDenied(response)) {
            ServerErrorHandler.showForceLogoutDialog();
            ServerErrorHandler.sendForceLogoutTokenAnalytics(response);
        }
    }
}
