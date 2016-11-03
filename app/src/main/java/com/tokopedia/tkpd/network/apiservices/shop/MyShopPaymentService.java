package com.tokopedia.tkpd.network.apiservices.shop;

import com.tokopedia.tkpd.network.apiservices.shop.apis.MyShopPaymentApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MyShopPaymentService extends AuthService<MyShopPaymentApi> {
    private static final String TAG = MyShopPaymentService.class.getSimpleName();


    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopPaymentApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_PAYMENT;
    }

    @Override
    public MyShopPaymentApi getApi() {
        return api;
    }
}
