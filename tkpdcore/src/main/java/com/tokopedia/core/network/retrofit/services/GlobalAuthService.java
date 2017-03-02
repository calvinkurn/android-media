package com.tokopedia.core.network.retrofit.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;

import okhttp3.Interceptor;
import retrofit2.Retrofit;


/**
 * @author Angga.Prasetiyo on 27/11/2015.
 */
public abstract class GlobalAuthService<T> extends BaseService<T> {

    protected abstract String getKeyAuth();

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientAuth(getKeyAuth()))
                .build();
    }
}