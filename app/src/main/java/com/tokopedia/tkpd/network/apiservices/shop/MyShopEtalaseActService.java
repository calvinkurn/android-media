package com.tokopedia.tkpd.network.apiservices.shop;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;
import com.tokopedia.tkpd.network.apiservices.shop.apis.MyShopEtalaseActApi;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MyShopEtalaseActService extends AuthService<MyShopEtalaseActApi> {
    private static final String TAG = MyShopEtalaseActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopEtalaseActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_ETALASE_ACTION;
    }

    @Override
    public MyShopEtalaseActApi getApi() {
        return api;
    }
}
