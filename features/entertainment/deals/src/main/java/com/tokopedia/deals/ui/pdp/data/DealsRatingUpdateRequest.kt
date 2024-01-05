package com.tokopedia.deals.ui.pdp.data

import com.google.gson.annotations.SerializedName

data class DealsRatingUpdateRequest(
    @SerializedName("rating")
    val rating: com.tokopedia.deals.ui.pdp.data.DealRatingRequest = com.tokopedia.deals.ui.pdp.data.DealRatingRequest()
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
