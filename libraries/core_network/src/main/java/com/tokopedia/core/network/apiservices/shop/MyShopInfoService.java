package com.tokopedia.core.network.apiservices.shop;

import com.tokopedia.core.network.apiservices.shop.apis.MyShopInfoApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class MyShopInfoService extends AuthService<MyShopInfoApi> {
    private static final String TAG = MyShopInfoService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopInfoApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_INFO;
    }

    @Override
    public MyShopInfoApi getApi() {
        return api;
    }
}
