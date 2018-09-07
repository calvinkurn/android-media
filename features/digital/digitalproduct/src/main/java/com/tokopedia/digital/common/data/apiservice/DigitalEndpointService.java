package com.tokopedia.digital.common.data.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public class DigitalEndpointService extends BaseService<DigitalApi> {

    public DigitalEndpointService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(DigitalApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.DIGITAL_API_DOMAIN + TkpdBaseURL.DigitalApi.VERSION;
    }

    @Override
    public DigitalApi getApi() {
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
