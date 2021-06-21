package com.tokopedia.tokomart.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TickerResponse(
        @SerializedName("ticker")
        @Expose
        val ticker: Tickers
)

data class Tickers(
        @SerializedName("tickers")
        @Expose
        val tickerList: List<Ticker>
)

data class Ticker(
        @SerializedName("id")
        @Expose
        val id: String,
        @SerializedName("title")
        @Expose
        val title: String,
        @SerializedName("message")
        @Expose
        val message: String,
        @SerializedName("color")
        @Expose
        val color: String
)