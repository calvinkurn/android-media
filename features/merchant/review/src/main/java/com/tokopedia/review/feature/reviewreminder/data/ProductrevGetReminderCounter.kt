package com.tokopedia.review.feature.reviewreminder.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReminderCounter(
        @SerializedName("totalProduct")
        @Expose
        val totalProduct: Int = 0,
        @SerializedName("totalBuyer")
        @Expose
        val totalBuyer: Int = 0,
        @SerializedName("isWhitelisted")
        @Expose
        val isWhitelisted: Boolean = false
)