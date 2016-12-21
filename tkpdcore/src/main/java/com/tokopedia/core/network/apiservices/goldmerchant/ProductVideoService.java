package com.tokopedia.core.network.apiservices.goldmerchant;

import com.tokopedia.core.network.apiservices.goldmerchant.apis.ProductVideoApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by kris on 11/9/16. Tokopedia
 */

public class ProductVideoService extends AuthService<ProductVideoApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ProductVideoApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.GOLD_MERCHANT_DOMAIN;
    }

    @Override
    public ProductVideoApi getApi() {
        return api;
    }
}