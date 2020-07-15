package com.tokopedia.review.feature.historydetails.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailReputation(
        @SerializedName("reputationID")
        @Expose
        val reputationId: Int = 0,
        @SerializedName("score")
        @Expose
        val score: Int = 0,
        @SerializedName("editable")
        @Expose
        val editable: Boolean = false,
        @SerializedName("lockTime")
        @Expose
        val lockTime: String = "",
        @SerializedName("lockTimeFormatted")
        @Expose
        val lockTimeFormatted: String = ""
)