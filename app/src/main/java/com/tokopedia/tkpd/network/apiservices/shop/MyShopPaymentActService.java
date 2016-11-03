package com.tokopedia.tkpd.network.apiservices.shop;

import com.tokopedia.tkpd.network.apiservices.shop.apis.MyShopPaymentActApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MyShopPaymentActService extends AuthService<MyShopPaymentActApi> {
    private static final String TAG = MyShopPaymentActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopPaymentActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_PAYMENT_ACTION;
    }

    @Override
    public MyShopPaymentActApi getApi() {
        return api;
    }
}
