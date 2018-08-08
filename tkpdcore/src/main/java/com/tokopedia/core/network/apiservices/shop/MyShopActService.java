package com.tokopedia.core.network.apiservices.shop;

import com.tokopedia.core.network.apiservices.shop.apis.MyShopActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MyShopActService extends AuthService<MyShopActApi> {
    private static final String TAG = MyShopActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_ACTION;
    }

    @Override
    public MyShopActApi getApi() {
        return api;
    }
}
