package com.tokopedia.core.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Bundle;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.accounts.apis.AccountsApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.session.model.TokenModel;
import com.tokopedia.core.session.presenter.Login;
import com.tokopedia.core.session.presenter.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

import static com.tokopedia.core.util.TokenSessionHelper.getExistingAccountAuthToken;

/**
 * @author ricoharisin .
 */

public class AccessTokenRefresh {

    public String refreshToken() throws IOException {
        Context context = MainApplication.getAppContext();

        SessionHandler sessionHandler = new SessionHandler(context);
        Map<String, String> params = new HashMap<>();

        params.put("grant_type", "refresh_token");
        params.put("refresh_token", getExistingAccountAuthToken(context, AccountGeneral.ACCOUNT_TYPE));

        AccountsService service = new AccountsService(new Bundle());
        Call<retrofit2.Response<String>> responseCall = service.getApi().getTokenSynchronous(params);

        retrofit2.Response<String> tokenResponse = null;
        try {
            tokenResponse = responseCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TokenModel model = null;
        if (tokenResponse != null) {
            model = new GsonBuilder().create().fromJson(tokenResponse.body(), TokenModel.class);
            sessionHandler.setToken(model.getAccessToken(), model.getTokenType());
        }

        return model.getRefreshToken();
    }
}
