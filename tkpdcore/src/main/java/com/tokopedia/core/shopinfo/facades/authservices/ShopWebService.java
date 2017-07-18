package com.tokopedia.core.shopinfo.facades.authservices;

import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author by errysuprayogi on 7/18/17.
 */

public class ShopWebService extends AuthService<ShopApi> {

    public ShopWebService(String overrideUrl) {
        super(overrideUrl);
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ShopApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return null;
    }

    @Override
    public ShopApi getApi() {
        return api;
    }
}
