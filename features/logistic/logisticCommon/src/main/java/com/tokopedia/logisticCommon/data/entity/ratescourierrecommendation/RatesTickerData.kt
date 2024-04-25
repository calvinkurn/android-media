package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class RatesTickerData(
    @SerializedName("TickerAction")
    val tickerAction: RatesTickerAction = RatesTickerAction(),
    @SerializedName("TickerType")
    val tickerType: String = "",
    @SerializedName("Content")
    val content: String = "",
    @SerializedName("Title")
    val title: String = ""
)

data class RatesTickerAction(
    @SerializedName("AppURL")
    val appUrl: String = "",
    @SerializedName("Label")
    val label: String = "",
    @SerializedName("WebURL")
    val webUrl: String = ""
)
