package com.tokopedia.deals.pdp.data

import com.google.gson.annotations.SerializedName

class DealsRecommendTrackingRequest(
    @SerializedName("message")
    val message: DealsRecommendMessage = DealsRecommendMessage(),
    @SerializedName("service")
    val service: String = ""
)

class DealsRecommendMessage(
    @SerializedName("action")
    val action: String = "",
    @SerializedName("product_id")
    val productId: Long = 0,
    @SerializedName("use_case")
    val useCase: String = "",
    @SerializedName("user_id")
    val userId: Long = 0
)
