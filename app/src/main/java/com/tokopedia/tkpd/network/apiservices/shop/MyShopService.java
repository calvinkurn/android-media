package com.tokopedia.tkpd.network.apiservices.shop;

import com.tokopedia.tkpd.network.apiservices.shop.apis.MyShopApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MyShopService extends AuthService<MyShopApi> {
    private static final String TAG = MyShopService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP;
    }

    @Override
    public MyShopApi getApi() {
        return api;
    }
}
