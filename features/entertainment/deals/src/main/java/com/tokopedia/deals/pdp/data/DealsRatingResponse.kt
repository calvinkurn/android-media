package com.tokopedia.deals.pdp.data

import com.google.gson.annotations.SerializedName

data class DealsRatingResponse(
    @SerializedName("data")
    val data: List<DealsRating>? = null,
    @SerializedName("status")
    val status: String = ""
)

data class DealsRating(
    @SerializedName("category_id")
    val categoryId: Long = 0,
    @SerializedName("product_id")
    val productId: Long = 0,
    @SerializedName("average_rating")
    val averageRating: Int = 0,
    @SerializedName("total_likes")
    val totalLikes: Int = 0,
    @SerializedName("is_liked")
    val isLiked: Boolean = false
)
