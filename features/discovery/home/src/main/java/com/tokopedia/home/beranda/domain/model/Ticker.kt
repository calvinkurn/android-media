package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 18/05/2020
 */

data class Ticker (
    @SerializedName("tickers")
    @Expose
    val tickers: List<Tickers> = listOf()

)

data class Tickers(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("layout")
        @Expose
        val layout: String = "",
        @SerializedName("ticker_type")
        @Expose
        var tickerType: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = ""
)