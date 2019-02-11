package com.tokopedia.core.network.apiservices.goldmerchant;

import com.tokopedia.core.network.apiservices.goldmerchant.apis.GoldMerchantApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by kris on 11/9/16. Tokopedia
 */

@Deprecated
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