package com.tokopedia.product.detail.data.model.review

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRatingCount(
    @SerializedName("ratingScore")
    @Expose
    val ratingScore: String = "",
    @SerializedName("totalRating")
    @Expose
    val totalRating: String = "",
    @SerializedName("totalReviewTextAndImage")
    @Expose
    val totalReviewTextAndImage: String = ""
)