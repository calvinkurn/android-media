package com.tokopedia.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.network.apiservices.accounts.apis.AccountsApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * @author ricoharisin .
 */

@Deprecated
public class SessionRefresh {

    private static final String USER_ID = "user_id";
    private static final String UUID_KEY = "uuid";

    private static final String DEVICE_ID_NEW = "device_id_new";
    private static final String OS_TYPE = "os_type";
    private static final String DEFAULT_ANDROID_OS_TYPE = "1";

    private final String accessToken;

    public SessionRefresh(String accessToken) {
        this.accessToken = accessToken;
    }

    public String refreshLogin() throws IOException {
        Context context = CoreNetworkApplication.getAppContext();
        UserSession userSession = new UserSession(context);
        SharedPreferences sharedPrefs = context.getSharedPreferences("LOGIN_SESSION", Context.MODE_PRIVATE);
        String tokenType = sharedPrefs.getString("TOKEN_TYPE", "");
        String authKey;
        if (TextUtils.isEmpty(accessToken)) {
            authKey = tokenType + " " + userSession.getAccessToken();
        } else {
            authKey = accessToken;
        }
        sharedPrefs = context.getSharedPreferences("LOGIN_UUID_KEY", Context.MODE_PRIVATE);
        String uuid = sharedPrefs.getString("uuid", "");
        RequestParams params = RequestParams.create();
        params.putString(UUID_KEY, uuid);
        params.putString(USER_ID, userSession.getUserId());
        Call<String> responseCall = getRetrofit(authKey)
                .create(AccountsApi.class).makeLoginsynchronous(
                        AuthUtil.generateParamsNetwork2(
                                CoreNetworkApplication.getAppContext(), params.getParameters()));
        return responseCall.execute().body();
    }

    public String gcmUpdate() throws IOException {
        Context context = CoreNetworkApplication.getAppContext();
        UserSession userSession = new UserSession(context);
        SharedPreferences sharedPrefs = context.getSharedPreferences("LOGIN_SESSION", Context.MODE_PRIVATE);
        String tokenType = sharedPrefs.getString("TOKEN_TYPE", "");
        String authKey;
        if (TextUtils.isEmpty(accessToken)) {
            authKey = tokenType + " " + userSession.getAccessToken();
        } else {
            authKey = accessToken;
        }

        RequestParams params = RequestParams.create();
        params.putString(DEVICE_ID_NEW, FCMCacheManager.getRegistrationId(context));
        params.putString(OS_TYPE, DEFAULT_ANDROID_OS_TYPE);
        params.putString(USER_ID, userSession.getUserId());
        Call<String> responseCall = getRetrofit(authKey)
                .create(AccountsApi.class).gcmUpdate(
                        AuthUtil.generateParamsNetwork2(
                                CoreNetworkApplication.getAppContext(), params.getParameters()));
        return responseCall.execute().body();
    }

    private Retrofit getRetrofit(String authKey) {
        return new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.ACCOUNTS_DOMAIN)
                .addConverterFactory(new StringResponseConverter())
                .client(OkHttpFactory.create().buildClientAccountsAuth(authKey, false, false))
                .build();
    }

}
