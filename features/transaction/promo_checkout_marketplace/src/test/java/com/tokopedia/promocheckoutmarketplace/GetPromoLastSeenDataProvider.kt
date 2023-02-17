package com.tokopedia.promocheckoutmarketplace

import com.google.gson.Gson
import com.tokopedia.promocheckoutmarketplace.data.response.GetPromoSuggestionResponse
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper

object GetPromoLastSeenDataProvider {

    private val gson = Gson()
    private val uiModelMapper = PromoCheckoutUiModelMapper()
    private val fileUtil = UnitTestFileUtils()

    fun provideGetPromoLastSeenSuccessEmpty(): GetPromoSuggestionResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_last_seen_success_empty.json"), GetPromoSuggestionResponse::class.java)
    }

    fun provideGetPromoLastSeenSuccessWithData(): GetPromoSuggestionResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/get_promo_last_seen_success_with_data.json"), GetPromoSuggestionResponse::class.java)
    }
}
