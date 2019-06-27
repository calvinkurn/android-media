package com.tokopedia.core.network.apiservices.product;

import com.tokopedia.core.network.apiservices.product.apis.ProductApi;
import com.tokopedia.core.network.apiservices.product.apis.PromoTopAdsApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 25/11/2015.
 */

@Deprecated
public class PromoTopAdsService extends AuthService<PromoTopAdsApi> {
    private static final String TAG = PromoTopAdsService.class.getSimpleName();

    public PromoTopAdsService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(PromoTopAdsApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOPADS_DOMAIN;
    }

    @Override
    public PromoTopAdsApi getApi() {
        return api;
    }
}
