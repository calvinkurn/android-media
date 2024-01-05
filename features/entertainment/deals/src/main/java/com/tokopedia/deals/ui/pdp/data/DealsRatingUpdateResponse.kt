package com.tokopedia.deals.ui.pdp.data

import com.google.gson.annotations.SerializedName

data class DealsRatingUpdateResponse(
    @SerializedName("data")
    val data: com.tokopedia.deals.ui.pdp.data.DealsRatingUpdate = com.tokopedia.deals.ui.pdp.data.DealsRatingUpdate(),
    @SerializedName("status")
    val status: String = ""
)

data class DealsRatingUpdate(
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("category_id")
    val categoryId: Long = 0,
    @SerializedName("product_id")
    val productId: Long = 0,
    @SerializedName("user_id")
    val userId: Long = 0,
    @SerializedName("rating")
    val rating: Int = 0,
    @SerializedName("is_liked")
    val isLiked: Boolean = false,
    @SerializedName("feedback")
    val feedback: String = "",
    @SerializedName("status")
    val status: Int = 0
)
