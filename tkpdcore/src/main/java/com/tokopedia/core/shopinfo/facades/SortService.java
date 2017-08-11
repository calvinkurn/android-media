package com.tokopedia.core.shopinfo.facades;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author by errysuprayogi on 7/26/17.
 */

public class SortService extends BaseService<SortApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(SortApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.ACE_DOMAIN;
    }

    @Override
    public SortApi getApi() {
        return api;
    }
}
