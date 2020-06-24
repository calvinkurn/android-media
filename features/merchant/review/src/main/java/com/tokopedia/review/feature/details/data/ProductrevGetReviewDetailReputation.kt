package com.tokopedia.review.feature.details.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailReputation(
        @SerializedName("reputationID")
        @Expose
        val reputationId: Int = 0,
        @SerializedName("score")
        @Expose
        val score: Int = 0
)