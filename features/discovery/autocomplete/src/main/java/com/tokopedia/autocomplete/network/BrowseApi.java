package com.tokopedia.autocomplete.network;


import com.tokopedia.autocomplete.domain.model.SearchResponse;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface BrowseApi {
    @GET(AutocompleteBaseURL.Ace.PATH_UNIVERSE_SEARCH)
    Observable<Response<SearchResponse>> getUniverseAutoComplete(
            @QueryMap HashMap<String, Object> param
    );

    @DELETE(AutocompleteBaseURL.Ace.PATH_DELETE_SEARCH)
    Observable<Response<Void>> deleteRecentSearch(
            @QueryMap HashMap<String, Object> parameters
    );
}
