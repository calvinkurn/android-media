package com.tokopedia.digital.common.data.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class DigitalGqlApiService extends AuthService<DigitalGqlApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(DigitalGqlApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.HOME_DATA_BASE_URL;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDigitalConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientDigitalAuth(TkpdBaseURL.DigitalApi.HMAC_KEY))
                .build();
    }

    @Override
    public DigitalGqlApi getApi() {
        return api;
    }

}
