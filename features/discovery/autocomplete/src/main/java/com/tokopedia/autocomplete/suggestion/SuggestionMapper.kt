package com.tokopedia.autocomplete.suggestion

import retrofit2.Response
import rx.functions.Func1

class SuggestionMapper : Func1<Response<SuggestionResponse>, SuggestionData> {

    override fun call(response: Response<SuggestionResponse>): SuggestionData? {
        if (response.isSuccessful && response.body() != null) {
            val searchResponse = response.body()

            return searchResponse?.data
        }
        return SuggestionData()
    }
}