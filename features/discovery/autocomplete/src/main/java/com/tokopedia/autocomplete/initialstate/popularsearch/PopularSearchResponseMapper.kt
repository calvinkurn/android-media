package com.tokopedia.autocomplete.initialstate.popularsearch

import com.tokopedia.autocomplete.initialstate.newfiles.InitialStateItem
import retrofit2.Response
import rx.functions.Func1

class PopularSearchResponseMapper : Func1<Response<PopularSearchResponse>, List<InitialStateItem>> {

    override fun call(response: Response<PopularSearchResponse>): List<InitialStateItem>? {
        if (response.isSuccessful && response.body() != null) {
            val searchResponse = response.body()

            return searchResponse?.data
        }
        return ArrayList()
    }
}