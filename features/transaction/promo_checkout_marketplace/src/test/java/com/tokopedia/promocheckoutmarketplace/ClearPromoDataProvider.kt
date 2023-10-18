package com.tokopedia.promocheckoutmarketplace

import com.google.gson.Gson
import com.tokopedia.promocheckoutmarketplace.data.response.ClearPromoResponse

object ClearPromoDataProvider {

    private val GSON = Gson()
    private val FILE_UTIL = UnitTestFileUtils()

    fun provideClearPromoResponseSuccess(): ClearPromoResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/clear_promo_response_success.json"), ClearPromoResponse::class.java)
    }

    fun provideClearPromoResponseFailed(): ClearPromoResponse {
        return GSON.fromJson(FILE_UTIL.getJsonFromAsset("assets/clear_promo_response_failed.json"), ClearPromoResponse::class.java)
    }
}
