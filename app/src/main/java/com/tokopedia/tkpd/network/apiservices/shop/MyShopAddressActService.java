package com.tokopedia.tkpd.network.apiservices.shop;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;
import com.tokopedia.tkpd.network.apiservices.shop.apis.MyShopAddressActApi;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MyShopAddressActService extends AuthService<MyShopAddressActApi> {
    private static final String TAG = MyShopAddressActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopAddressActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_ADDRESS_ACTION;
    }

    @Override
    public MyShopAddressActApi getApi() {
        return api;
    }
}
