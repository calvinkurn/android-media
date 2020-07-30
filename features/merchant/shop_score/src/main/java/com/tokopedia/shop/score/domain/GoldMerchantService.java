package com.tokopedia.shop.score.domain;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

public class GoldMerchantService extends AuthService<GoldMerchantApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(GoldMerchantApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.GOLD_MERCHANT_DOMAIN;
    }

    @Override
    public GoldMerchantApi getApi() {
        return api;
    }
}