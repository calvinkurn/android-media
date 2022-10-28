package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName

data class AutoAdsParam(
    @SerializedName("input")
    val input: Input,
) {
    data class Input(
        @SerializedName("action")
        val action: String = "",
        @SerializedName("channel")
        val channel: String = "",
        @SerializedName("dailyBudget")
        val dailyBudget: Int = 0,
        @SerializedName("shopID")
        val shopID: String = "",
        @SerializedName("source")
        val source: String = "",
    )
}