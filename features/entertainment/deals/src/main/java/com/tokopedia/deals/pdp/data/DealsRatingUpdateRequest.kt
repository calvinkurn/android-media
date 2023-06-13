package com.tokopedia.deals.pdp.data

import com.google.gson.annotations.SerializedName

data class DealsRatingUpdateRequest(
    @SerializedName("rating")
    val rating: DealRatingRequest = DealRatingRequest()
)

data class DealRatingRequest(
    @SerializedName("feedback")
    val feedback: String = "",
    @SerializedName("is_liked")
    val isLiked: String = "false",
    @SerializedName("product_id")
    val productId: Long = 0,
    @SerializedName("user_id")
    val userId: Long = 0,
    @SerializedName("rating")
    val rating: Int = 0
)
