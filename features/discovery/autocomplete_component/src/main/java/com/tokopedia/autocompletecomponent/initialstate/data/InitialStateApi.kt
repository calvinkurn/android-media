package com.tokopedia.autocompletecomponent.initialstate.data


import com.tokopedia.autocompletecomponent.network.AutocompleteBaseURL
import retrofit2.Response
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
