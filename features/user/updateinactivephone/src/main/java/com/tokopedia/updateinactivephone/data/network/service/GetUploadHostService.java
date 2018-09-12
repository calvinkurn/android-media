package com.tokopedia.updateinactivephone.data.network.service;

import android.os.Bundle;

import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.updateinactivephone.data.network.api.UploadImageApi;
import com.tokopedia.updateinactivephone.di.UpdateInactivePhoneScope;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Provides;
import retrofit2.Retrofit;

public class GetUploadHostService {
    private static final String WS_SERVICE = "WS_SERVICE";

    public static final String ACCOUNTS = "ACCOUNTS";
    public static final String WS = "WS";

    public static final String AUTH_KEY = "AUTH_KEY";
    public static final String WEB_SERVICE = "WEB_SERVICE";
    public static final String USING_HMAC = "USING_HMAC";
    public static final String IS_BASIC = "IS_BASIC";
    public static final String USING_BOTH_AUTHORIZATION = "USING_BOTH_AUTHORIZATION";


    private static final String TAG = GetUploadHostService.class.getSimpleName();
    protected UploadImageApi api;

    @Inject
    public GetUploadHostService() {

        String authKey = "";//bundle.getString(AUTH_KEY, "");
        boolean isUsingHMAC = false;// bundle.getBoolean(USING_HMAC, false);
        boolean isUsingBothAuthorization = false;//bundle.getBoolean(USING_BOTH_AUTHORIZATION, false);

        initApiService(RetrofitFactory.createRetrofitDefaultConfig(getBaseUrl())
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy())
                        .buildClientAccountsAuth(authKey,
                                isUsingHMAC,
                                isUsingBothAuthorization))
                .build());
    }

    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(UploadImageApi.class);
    }

    protected String getBaseUrl() {

        return "https://ws.tokopedia.com/";

    }

    public UploadImageApi getApi() {
        return api;
    }
}
