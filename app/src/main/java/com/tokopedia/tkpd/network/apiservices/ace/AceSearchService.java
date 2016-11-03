package com.tokopedia.tkpd.network.apiservices.ace;

import com.tokopedia.tkpd.network.apiservices.ace.apis.SearchApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 19/01/2016.
 */
public class AceSearchService extends BaseService<SearchApi> {
    private static final String TAG = AceSearchService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(SearchApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Ace.URL_SEARCH;
    }

    @Override
    public SearchApi getApi() {
        return api;
    }
}
