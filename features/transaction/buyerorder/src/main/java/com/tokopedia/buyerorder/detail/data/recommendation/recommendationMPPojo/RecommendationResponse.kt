package com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

        @field:SerializedName("rechargeFavoriteRecommendationList")
        val rechargeFavoriteRecommendationList: RechargeFavoriteRecommendationList?
)