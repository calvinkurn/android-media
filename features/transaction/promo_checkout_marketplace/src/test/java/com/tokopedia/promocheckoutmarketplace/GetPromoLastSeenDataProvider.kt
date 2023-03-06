package com.tokopedia.promocheckoutmarketplace

import com.google.gson.Gson
import com.tokopedia.promocheckoutmarketplace.data.response.GetPromoSuggestionResponse

object GetPromoLastSeenDataProvider {

    private val GSON = Gson()
    private val FILE_UTIL = UnitTestFileUtils()

    fun provideGetPromoLastSeenSuccessEmpty(): GetPromoSuggestionResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_last_seen_success_empty.json"), GetPromoSuggestionResponse::class.java)
    }

    fun provideGetPromoLastSeenSuccessWithData(): GetPromoSuggestionResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/get_promo_last_seen_success_with_data.json"), GetPromoSuggestionResponse::class.java)
    }
}
