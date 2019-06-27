package com.tokopedia.core.network.retrofit.services;

import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;

import retrofit2.Retrofit;


/**
 * @author Angga.Prasetiyo on 27/11/2015.
 */

@Deprecated
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