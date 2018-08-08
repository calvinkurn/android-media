package com.tokopedia.core.network.apiservices.product;

import com.tokopedia.core.network.apiservices.product.apis.PromoApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class PromoService extends AuthService<PromoApi> {
    private static final String TAG = PromoService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(PromoApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Product.URL_PROMO;
    }

    @Override
    public PromoApi getApi() {
        return api;
    }
}
