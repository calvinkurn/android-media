package com.tokopedia.promousage.util

import com.google.gson.Gson
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse

object DataProvider {

    private val gson = Gson()
    private val fileUtil = FileUtil()

    fun provideGetPromoListRecommendationSuccessNoAttemptPromo(): GetPromoListRecommendationResponse {
        return fileUtil.getJsonFromAsset("assets/get_promo_list_recommendation_success_no_attempt_promo.json")
            .asGetPromoListRecommendationResponse()
    }

    private fun String.asGetPromoListRecommendationResponse(): GetPromoListRecommendationResponse {
        return gson.fromJson(this, GetPromoListRecommendationResponse::class.java)
    }
}
