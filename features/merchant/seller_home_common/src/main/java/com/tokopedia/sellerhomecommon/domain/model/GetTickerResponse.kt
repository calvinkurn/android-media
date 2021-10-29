package com.tokopedia.sellerhomecommon.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetTickerResponse(
        @Expose
        @SerializedName("ticker")
        val ticker: TickerModel? = TickerModel()
)

data class TickerModel(
        @Expose
        @SerializedName("tickers")
        val tickers: List<TickerItemModel>? = emptyList()
)

data class TickerItemModel(
        @Expose
        @SerializedName("id")
        val id: String? = "",
        @Expose
        @SerializedName("message")
        val message: String? = "",
        @Expose
        @SerializedName("ticker_type")
        val tickerType: Int? = 0,
        @Expose
        @SerializedName("title")
        val title: String? = ""
)