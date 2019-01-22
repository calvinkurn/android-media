package com.tokopedia.core.network.apiservices.search;

import com.tokopedia.core.network.apiservices.search.apis.SearchApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */

@Deprecated
public class SearchService extends AuthService<SearchApi> {
    private static final String TAG = SearchService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(SearchApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Search.URL_SEARCH;
    }

    @Override
    public SearchApi getApi() {
        return api;
    }
}
