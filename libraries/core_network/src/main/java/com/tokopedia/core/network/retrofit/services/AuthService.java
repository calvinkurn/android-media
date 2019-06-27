package com.tokopedia.core.network.retrofit.services;

import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 27/11/2015.
 */

@Deprecated
public abstract class AuthService<T> extends BaseService<T> {

    public AuthService() {
        super();
    }

    public AuthService(String overrideUrl) {
        super(overrideUrl);
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientDefaultAuth())
                .build();
    }
}