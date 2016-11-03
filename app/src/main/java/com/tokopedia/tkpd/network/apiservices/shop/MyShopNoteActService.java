package com.tokopedia.tkpd.network.apiservices.shop;

import com.tokopedia.tkpd.network.apiservices.shop.apis.MyShopNoteActApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MyShopNoteActService extends AuthService<MyShopNoteActApi> {
    private static final String TAG = MyShopNoteActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopNoteActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_NOTE_ACTION;
    }

    @Override
    public MyShopNoteActApi getApi() {
        return api;
    }
}
