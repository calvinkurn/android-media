package com.tokopedia.core.network.apiservices.search;

import com.tokopedia.core.network.apiservices.search.apis.SearchSuggestionApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Toped18 on 7/1/2016.
 */
public class SearchSuggestionService extends AuthService<SearchSuggestionApi> {
    private static final String TAG = SearchSuggestionService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(SearchSuggestionApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Search.URL_SEARCH_SUGGESTION;
    }

    @Override
    public SearchSuggestionApi getApi() {
        return api;
    }
}
