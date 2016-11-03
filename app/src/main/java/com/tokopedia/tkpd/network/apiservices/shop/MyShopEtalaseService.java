package com.tokopedia.tkpd.network.apiservices.shop;

import com.tokopedia.tkpd.network.apiservices.shop.apis.MyShopEtalaseApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 02/12/2015.
 */
public class MyShopEtalaseService extends AuthService<MyShopEtalaseApi> {
    private static final String TAG = MyShopEtalaseService.class.getSimpleName();

    public MyShopEtalaseService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopEtalaseApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_ETALASE;
    }

    @Override
    public MyShopEtalaseApi getApi() {
        return api;
    }
}
