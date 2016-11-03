package com.tokopedia.tkpd.network.apiservices.shop;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;
import com.tokopedia.tkpd.network.apiservices.shop.apis.MyShopInfoActApi;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MyShopInfoActService extends AuthService<MyShopInfoActApi> {
    private static final String TAG = MyShopInfoActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopInfoActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_INFO_ACTION;
    }

    @Override
    public MyShopInfoActApi getApi() {
        return api;
    }
}
