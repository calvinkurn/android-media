package com.tokopedia.core.shopinfo.facades.authservices;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;


/**
 * Created by Tkpd_Eka on 12/3/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */

@Deprecated
public class ShopService extends AuthService<ShopApi> {

    public ShopService() {
        super();
    }

    public ShopService(String overrideUrl) {
        super(overrideUrl);
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ShopApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_SHOP;
    }

    @Override
    public ShopApi getApi() {
        return api;
    }
}
