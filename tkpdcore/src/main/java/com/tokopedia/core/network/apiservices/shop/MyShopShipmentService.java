package com.tokopedia.core.network.apiservices.shop;

import com.tokopedia.core.network.apiservices.shop.apis.MyShopShipmentApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MyShopShipmentService extends AuthService<MyShopShipmentApi> {
    private static final String TAG = MyShopShipmentService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopShipmentApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_SHIPMENT;
    }

    @Override
    public MyShopShipmentApi getApi() {
        return api;
    }
}
