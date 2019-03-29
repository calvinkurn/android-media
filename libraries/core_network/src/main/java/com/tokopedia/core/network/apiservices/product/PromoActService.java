package com.tokopedia.core.network.apiservices.product;

import com.tokopedia.core.network.apiservices.product.apis.PromoActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */

@Deprecated
public class PromoActService extends AuthService<PromoActApi>{
    private static final String TAG = PromoActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(PromoActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Product.URL_PROMO_ACTION;
    }

    @Override
    public PromoActApi getApi() {
        return api;
    }
}
