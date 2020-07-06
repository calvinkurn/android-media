package com.tokopedia.buyerorder.detail.data.recommendationMPPojo

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

        @field:SerializedName("rechargeFavoriteRecommendationList")
        val rechargeFavoriteRecommendationList: RechargeFavoriteRecommendationList?
)