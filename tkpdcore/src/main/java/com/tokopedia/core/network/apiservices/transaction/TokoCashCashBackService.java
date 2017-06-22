package com.tokopedia.core.network.apiservices.transaction;

import com.tokopedia.core.network.apiservices.transaction.apis.TokoCashApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by kris on 6/15/17. Tokopedia
 */

public class TokoCashCashBackService extends AuthService<TokoCashApi>{

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TokoCashApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOKO_CASH_DOMAIN;
    }

    @Override
    public TokoCashApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDigitalConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientTokoCashAuth(TkpdBaseURL.TopCash.HMAC_KEY))
                .build();
    }
}
