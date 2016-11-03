package com.tokopedia.tkpd.network.apiservices.search;

import com.tokopedia.tkpd.network.apiservices.search.apis.CatalogApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class CatalogService extends AuthService<CatalogApi> {
    private static final String TAG = CatalogService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(CatalogApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Search.URL_CATALOG;
    }

    @Override
    public CatalogApi getApi() {
        return api;
    }
}
