package com.tokopedia.tkpd.network.apiservices.shop;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;
import com.tokopedia.tkpd.network.apiservices.shop.apis.MyShopAddressApi;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MyShopAddressService extends AuthService<MyShopAddressApi> {
    private static final String TAG = MyShopAddressService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopAddressApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_ADDRESS;
    }

    @Override
    public MyShopAddressApi getApi() {
        return api;
    }
}
