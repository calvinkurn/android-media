package com.tokopedia.tkpd.thankyou.data.source.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.BaseService;
import com.tokopedia.network.utils.OkHttpRetryPolicy;

import retrofit2.Retrofit;

public class DigitalTrackerService extends BaseService<DigitalTrackerApi> {

    public DigitalTrackerService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(DigitalTrackerApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.DIGITAL_API_DOMAIN;
    }

    @Override
    public DigitalTrackerApi getApi() {
        return api;
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
    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy();
    }
}