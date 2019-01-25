package com.tokopedia.core.network.apiservices.transaction;

import com.tokopedia.core.network.apiservices.transaction.apis.CreditCardAuthApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by kris on 10/13/17. Tokopedia
 */

public class CreditCardAuthService extends AuthService<CreditCardAuthApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(CreditCardAuthApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.SCROOGE_DOMAIN;
    }

    @Override
    public CreditCardAuthApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientCreditCardAuth()).build();
    }

}
