package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Stats(
        @SerializedName("count_review")
        @Expose
        val countReview: Int = 0,

        @SerializedName("count_talk")
        @Expose
        val countTalk: Int = 0,

        @SerializedName("count_view")
        @Expose
        val countView: Int = 0,

        @SerializedName("rating")
        @Expose
        val rating: Int = 0
)

data class TxStats(
        @SerializedName("sold")
        @Expose
        val sold: Int = 0,

        @SerializedName("tx_reject")
        @Expose
        val txReject: Int = 0,

        @SerializedName("tx_success")
        @Expose
        val txSuccess: Int = 0
)