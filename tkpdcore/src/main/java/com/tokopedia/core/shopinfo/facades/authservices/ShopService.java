package com.tokopedia.core.shopinfo.facades.authservices;


import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;


/**
 * Created by Tkpd_Eka on 12/3/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ShopService extends AuthService<ShopApi> {

    private static final String URL_SHOP = "https://ws-staging.tokopedia.com/v4/shop/";

    public ShopService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ShopApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return URL_SHOP;
    }

    @Override
    public ShopApi getApi() {
        return api;
    }
}
