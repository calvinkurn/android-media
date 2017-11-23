package com.tokopedia.core.network.apiservices.transaction;

import com.tokopedia.core.network.apiservices.transaction.apis.CreditCardVaultApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by kris on 8/21/17. Tokopedia
 */

public class CreditCardVaultService extends AuthService<CreditCardVaultApi>{

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(CreditCardVaultApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.SCROOGE_CREDIT_CARD_DOMAIN;
    }

    @Override
    public CreditCardVaultApi getApi() {
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
