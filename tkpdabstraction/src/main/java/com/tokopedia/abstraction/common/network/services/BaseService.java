package com.tokopedia.abstraction.common.network.services;

import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.factory.OkHttpFactory;
import com.tokopedia.abstraction.common.network.factory.RetrofitFactory;

import retrofit2.Retrofit;


/**
 * @author Angga.Prasetiyo on 26/11/2015.
 */
public abstract class BaseService<T> {
    private static final String TAG = BaseService.class.getSimpleName();

    protected T api;

    protected abstract void initApiService(Retrofit retrofit);

    protected abstract String getBaseUrl();

    public abstract T getApi();

    public BaseService() {
        initApiService(createRetrofitInstance(getBaseUrl()));
    }

    public BaseService(String overrideUrl) {
        initApiService(createRetrofitInstance(overrideUrl));
    }

    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientNoAuth())
                .build();
    }

    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

}