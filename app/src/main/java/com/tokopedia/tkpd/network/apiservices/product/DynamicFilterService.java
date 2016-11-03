package com.tokopedia.tkpd.network.apiservices.product;

import com.tokopedia.tkpd.network.apiservices.product.apis.DynamicFilter;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

import static com.tokopedia.tkpd.network.apiservices.product.apis.DynamicFilter.DYNAMIC_FILTER_URL;

/**
 * @author sebastianuskh on 9/22/16.
 */
public class DynamicFilterService extends AuthService<DynamicFilter> {

    public DynamicFilterService() {
        super(DYNAMIC_FILTER_URL, true);
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(DynamicFilter.class);
    }

    @Override
    protected String getBaseUrl() {
        return null;
    }

    @Override
    public DynamicFilter getApi() {
        return api;
    }
}
