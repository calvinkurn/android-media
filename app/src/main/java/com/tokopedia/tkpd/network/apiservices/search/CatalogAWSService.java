package com.tokopedia.tkpd.network.apiservices.search;

import com.tokopedia.tkpd.network.apiservices.search.apis.CatalogAWSApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author Tkpd_Eka on 1/19/2016.
 */
public class CatalogAWSService extends BaseService<CatalogAWSApi> {

    private static final String TAG = CatalogService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(CatalogAWSApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.ACE_DOMAIN;
    }

    @Override
    public CatalogAWSApi getApi() {
        return api;
    }
}
