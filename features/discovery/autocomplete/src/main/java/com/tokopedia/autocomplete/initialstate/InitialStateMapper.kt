package com.tokopedia.autocomplete.initialstate

import java.util.ArrayList

import retrofit2.Response
import rx.functions.Func1

class InitialStateMapper : Func1<Response<InitialStateResponse>, List<InitialStateData>> {

    override fun call(response: Response<InitialStateResponse>): List<InitialStateData>? {
        if (response.isSuccessful && response.body() != null) {
            val searchResponse = response.body()

            return searchResponse?.data
        }
        return ArrayList()
    }
}
