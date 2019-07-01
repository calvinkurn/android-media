package com.tokopedia.discovery.catalog.network.apiservices;

import com.tokopedia.config.url.TokopediaUrl;
import com.tokopedia.core.network.apiservices.search.CatalogService;
import com.tokopedia.discovery.catalog.network.apiservices.apis.CatalogAWSApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;

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
        return TokopediaUrl.Companion.getInstance().getACE();
    }

    @Override
    public CatalogAWSApi getApi() {
        return api;
    }
}
