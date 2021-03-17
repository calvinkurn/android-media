package com.tokopedia.review.feature.reviewreminder.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReminderData(
        @SerializedName("productID")
        @Expose
        val productID: String = "",
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("productThumbnail")
        @Expose
        val productThumbnail: String = "",
        @SerializedName("avgRating")
        @Expose
        val avgRating: Float = 0F,
        @SerializedName("ratingCount")
        @Expose
        val ratingCount: Int = 0,
        @SerializedName("buyerCount")
        @Expose
        val buyerCount: Int = 0
)