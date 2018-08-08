package com.tokopedia.tkpd.home.service;

import com.tokopedia.core.network.apiservices.etc.apis.home.ProductFeedApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * Created by m.normansyah on 1/22/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ProductFeedService extends BaseService<ProductFeedApi> {

    private static final String TAG = ProductFeedService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ProductFeedApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Etc.URL_HOME;
    }

    @Override
    public ProductFeedApi getApi() {
        return api;
    }
}
