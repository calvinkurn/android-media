package com.tokopedia.core.network.retrofit.services;

import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;

import retrofit2.Retrofit;

/**
 * @author by alvarisi on 12/9/16.
 */

@Deprecated
public abstract class BearerService<T> extends BaseService<T>{
    protected T mApi;
    protected String mToken;

    public BearerService(String mToken) {
        this.mToken = mToken;
        initApiService(createRetrofitInstance(getBaseUrl()));
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientBearerAuth(getOauthAuthorization()))
                .build();
    }

    protected abstract String getBaseUrl();

    protected abstract String getOauthAuthorization();

    protected abstract void initApiService(Retrofit retrofit);

    public abstract T getApi();
}
