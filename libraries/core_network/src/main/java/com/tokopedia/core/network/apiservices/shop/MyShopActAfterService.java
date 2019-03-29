package com.tokopedia.core.network.apiservices.shop;

import com.tokopedia.core.network.apiservices.shop.apis.OpenShopPicture;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 9/28/16.
 */

@Deprecated
public class MyShopActAfterService extends AuthService<OpenShopPicture> {

    public MyShopActAfterService() {
        super();
    }

    public MyShopActAfterService(String overrideUrl) {
        super(overrideUrl);
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(OpenShopPicture.class);
    }

    @Override
    protected String getBaseUrl() {
        return null;
    }

    @Override
    public OpenShopPicture getApi() {
        return api;
    }
}
