package com.tokopedia.promocheckoutmarketplace

import com.google.gson.Gson
import com.tokopedia.promocheckoutmarketplace.data.response.ClearPromoResponse
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper

object ClearPromoDataProvider {

    private val gson = Gson()
    private val uiModelMapper = PromoCheckoutUiModelMapper()
    private val fileUtil = UnitTestFileUtils()

    fun provideClearPromoResponseSuccess(): ClearPromoResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/clear_promo_response_success.json"), ClearPromoResponse::class.java)
    }

    fun provideClearPromoResponseFailed(): ClearPromoResponse {
        return gson.fromJson(fileUtil.getJsonFromAsset("assets/clear_promo_response_failed.json"), ClearPromoResponse::class.java)
    }
}
