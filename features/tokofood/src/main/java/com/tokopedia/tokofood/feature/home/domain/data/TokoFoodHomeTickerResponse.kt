package com.tokopedia.tokofood.feature.home.domain.data

import com.google.gson.annotations.SerializedName

data class TokoFoodHomeTickerResponse(
    @SerializedName("ticker")
    val ticker: Tickers
)

data class Tickers(
    @SerializedName("tickers")
    val tickerList: List<TickerItem>
)

data class TickerItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("layout")
    val layout: String,
    @SerializedName("ticker_type")
    val tickerType: Int
)