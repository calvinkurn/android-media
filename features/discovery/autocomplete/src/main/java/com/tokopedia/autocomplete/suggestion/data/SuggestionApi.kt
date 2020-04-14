package com.tokopedia.autocomplete.suggestion.data

import com.tokopedia.autocomplete.network.AutocompleteBaseURL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url
import rx.Observable
import java.util.HashMap

interface SuggestionApi {
    @GET(AutocompleteBaseURL.Ace.PATH_SUGGESTION)
    fun getSuggestionResponse(
            @QueryMap param: HashMap<String, Any>
    ): Observable<Response<SuggestionResponse>>

    @POST
    fun hitSuggestionUrlTracker(@Url url: String): Observable<Response<Void>>
}