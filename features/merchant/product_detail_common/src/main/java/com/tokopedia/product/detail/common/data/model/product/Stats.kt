package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Stats(
        @SerializedName("countReview")
        @Expose
        val countReview: String = "",

        @SerializedName("countTalk")
        @Expose
        val countTalk: String = "",

        @SerializedName("rating")
        @Expose
        val rating: Float = 0f
)