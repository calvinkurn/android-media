package com.tokopedia.core.gcm.network;

import com.tokopedia.network.utils.OkHttpRetryPolicy;

import retrofit2.Retrofit;


/**
 * @author Angga.Prasetiyo on 26/11/2015.
 */
public abstract class BaseService<T> {

    protected T api;

    protected abstract void initApiService(Retrofit retrofit);

    protected abstract String getBaseUrl();

    public abstract T getApi();

    public BaseService() {
        initApiService(createRetrofitInstance(getBaseUrl()));
    }

    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return null;
    }

    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

}