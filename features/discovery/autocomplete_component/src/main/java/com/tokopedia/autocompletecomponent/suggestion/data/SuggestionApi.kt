package com.tokopedia.autocompletecomponent.suggestion.data

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Url
import rx.Observable

interface SuggestionApi {
    @POST
    fun hitSuggestionUrlTracker(@Url url: String): Observable<Response<Void>>
}