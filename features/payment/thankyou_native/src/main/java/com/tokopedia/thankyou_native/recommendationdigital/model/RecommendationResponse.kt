package com.tokopedia.thankyou_native.recommendationdigital.model

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

        @field:SerializedName("rechargeFavoriteRecommendationList")
        val rechargeFavoriteRecommendationList: RechargeFavoriteRecommendationList?
)