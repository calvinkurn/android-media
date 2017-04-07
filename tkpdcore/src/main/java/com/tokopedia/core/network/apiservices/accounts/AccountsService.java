package com.tokopedia.core.network.apiservices.accounts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.accounts.apis.AccountsApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.AccountsInterceptor;
import com.tokopedia.core.analytics.TrackingUtils;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author stevenfredian on 5/25/16.
 */
public class AccountsService {

    public static final String ACCOUNTS = "ACCOUNTS";
    public static final String WS = "WS";

    public static final String AUTH_KEY = "AUTH_KEY";
    public static final String WEB_SERVICE = "WEB_SERVICE";
    public static final String USING_HMAC = "USING_HMAC";
    public static final String USING_BOTH_AUTHORIZATION = "USING_BOTH_AUTHORIZATION";


    private static final String TAG = AccountsService.class.getSimpleName();
    protected AccountsApi api;

    public AccountsService(Bundle bundle) {

        String authKey = bundle.getString(AUTH_KEY, "");
        String webService = bundle.getString(WEB_SERVICE, ACCOUNTS);
        boolean isUsingHMAC = bundle.getBoolean(USING_HMAC, false);
        boolean isUsingBothAuthorization = bundle.getBoolean(USING_BOTH_AUTHORIZATION, false);



        initApiService(RetrofitFactory.createRetrofitDefaultConfig(getBaseUrl(webService))
                        .client(OkHttpFactory.create()
                                .addOkHttpRetryPolicy(OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy())
                                .buildClientAccountsAuth(authKey, isUsingHMAC, isUsingBothAuthorization))
                        .build());
}

    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(AccountsApi.class);
    }

    protected String getBaseUrl(String webService) {
        switch (webService) {
            case WS:
                return TkpdBaseURL.BASE_DOMAIN;
            case ACCOUNTS:
                return TkpdBaseURL.ACCOUNTS_DOMAIN;
            default:
                throw new RuntimeException("unknown Base URL");
        }
    }

    public AccountsApi getApi() {
        return api;
    }

}
