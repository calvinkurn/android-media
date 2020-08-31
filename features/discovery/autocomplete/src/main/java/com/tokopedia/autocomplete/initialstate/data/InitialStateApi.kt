package com.tokopedia.autocomplete.initialstate.data


import com.tokopedia.autocomplete.initialstate.InitialStateResponse
import com.tokopedia.autocomplete.network.AutocompleteBaseURL
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
}
