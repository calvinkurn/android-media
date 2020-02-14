package com.tokopedia.autocomplete.initialstate.newfiles


import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchResponse
import com.tokopedia.autocomplete.network.AutocompleteBaseURL
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable
import java.util.*

interface InitialStateApi {
    @GET(AutocompleteBaseURL.Ace.PATH_INITIAL_STATE)
    fun getInitialState(
            @QueryMap param: HashMap<String, Any>
    ): Observable<Response<InitialStateResponse>>

    @DELETE(AutocompleteBaseURL.Ace.PATH_INITIAL_STATE)
    fun deleteRecentSearch(
            @QueryMap parameters: HashMap<String, Any>
    ): Observable<Response<Void>>

    @DELETE(AutocompleteBaseURL.Ace.PATH_POPULAR_SEARCH)
    fun refreshPopularSearch(
            @QueryMap parameters: HashMap<String, Any>
    ): Observable<Response<PopularSearchResponse>>

    @GET(AutocompleteBaseURL.Ace.PATH_INITIAL_STATE)
    fun getInitialStateFlow(
            @QueryMap param: HashMap<String, Any>
    ): InitialStateResponse
}
