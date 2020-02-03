package com.tokopedia.sellerhomedrawer.domain.service;

import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.sellerhomedrawer.domain.factory.SellerRetrofitFactory;

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
        return SellerRetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientDefaultAuth())
                .build();
    }
}