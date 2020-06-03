package com.tokopedia.review.feature.review.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetFormReputation(
        @SerializedName("score")
        @Expose
        val score: Int = 0,
        @SerializedName("editable")
        @Expose
        val editablle: Boolean = false
)