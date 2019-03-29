package com.tokopedia.core.network.apiservices.shop;

import com.tokopedia.core.network.apiservices.shop.apis.MyShopOrderApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class MyShopOrderService extends AuthService<MyShopOrderApi> {
    private static final String TAG = MyShopOrderService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopOrderApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_ORDER;
    }

    @Override
    public MyShopOrderApi getApi() {
        return api;
    }
}
