package com.tokopedia.core.network.apiservices.shop;

import com.tokopedia.core.network.apiservices.shop.apis.OpenShopPicture;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 9/28/16.
 */

public class MyShopActAfterService extends AuthService<OpenShopPicture> {
    String baseUrl;

    public MyShopActAfterService(String baseUrl) {
        super(baseUrl, 1);
        this.baseUrl = baseUrl;
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(OpenShopPicture.class);
    }

    @Override
    protected String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public OpenShopPicture getApi() {
        return api;
    }
}
