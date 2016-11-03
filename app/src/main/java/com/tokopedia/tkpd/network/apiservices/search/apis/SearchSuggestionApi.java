package com.tokopedia.tkpd.network.apiservices.search.apis;

import com.tokopedia.tkpd.discovery.model.history.searchSuggestion.SearchDataModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Toped18 on 7/1/2016.
 */
public interface SearchSuggestionApi {
    @GET("universe/v1?device=android&source=searchbar")
    Observable<Response<SearchDataModel>> searchSuggestion(
            @Query("q") String querySearch,
            @Query("unique_id") String unique_id,
            @Query("count") String count

    );

    @DELETE("universe/v1?device=android&source=searchbar")
    Observable<Response<Void>> deleteHistorySearch(
            @Query("q") String querySearch,
            @Query("unique_id") String unique_id,
            @Query("clear_all") String clearAll
    );

}
