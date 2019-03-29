package com.tokopedia.core.network.apiservices.transaction;

import com.tokopedia.core.network.apiservices.transaction.apis.DynamicPaymentApi;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author Default05 on 2/10/2016.
 */

@Deprecated
public class DynamicPaymentService extends BaseService<DynamicPaymentApi> {

    private String baseUrl;

    public DynamicPaymentService(String bUrl) {
        baseUrl = bUrl;
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(DynamicPaymentApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public DynamicPaymentApi getApi() {
        return api;
    }
}
