package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Stats(
        @SerializedName("countView")
        val countView: Int = 0,
        @SerializedName("countReview")
        val countReview: Int = 0,
        @SerializedName("countTalk")
        val countTalk: Int = 0,
        @SerializedName("rating")
        val rating: Int = 0
)