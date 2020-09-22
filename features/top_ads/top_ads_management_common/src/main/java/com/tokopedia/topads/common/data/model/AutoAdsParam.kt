package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName

data class AutoAdsParam(
        @SerializedName("input")
        val input: Input
) {
    data class Input(
            @SerializedName("action")
            val action: String,
            @SerializedName("channel")
            val channel: String,
            @SerializedName("dailyBudget")
            val dailyBudget: Int,
            @SerializedName("shopId")
            val shopId: Int,
            @SerializedName("source")
            val source: String
    )
}