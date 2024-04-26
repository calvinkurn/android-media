package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class RatesTickerData(
    @SerializedName("actions")
    val tickerAction: RatesTickerAction = RatesTickerAction(),
    @SerializedName("type")
    val tickerType: String = "",
    @SerializedName("text")
    val content: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("position")
    val position: String = ""
)

data class RatesTickerAction(
    @SerializedName("app_url")
    val appUrl: String = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("web_url")
    val webUrl: String = ""
)
