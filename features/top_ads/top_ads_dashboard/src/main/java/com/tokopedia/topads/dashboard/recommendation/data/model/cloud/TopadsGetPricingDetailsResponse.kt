package com.tokopedia.topads.dashboard.recommendation.data.model.cloud

import com.google.gson.annotations.SerializedName

data class TopAdsGetPricingDetailsResponse(
    @SerializedName("topadsGetPricingDetails")
    val topAdsGetPricingDetails: TopAdsGetPricingDetails = TopAdsGetPricingDetails()
) {
    data class TopAdsGetPricingDetails(
        @SerializedName("maxBid")
        val maxBid: Int = 0,
        @SerializedName("minBid")
        val minBid: Int = 0
    )
}
