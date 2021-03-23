package com.tokopedia.stickylogin.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StickyLoginTickerDataModel(
    @SerializedName("tickers")
    @Expose
    val tickerDataModels: List<TickerDetailDataModel> = ArrayList()
) {
    data class TickerResponse(
        @SerializedName("ticker")
        @Expose
        val response: StickyLoginTickerDataModel = StickyLoginTickerDataModel()
    )

    data class TickerDetailDataModel(
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("layout")
        @Expose
        val layout: String = ""
    )
}