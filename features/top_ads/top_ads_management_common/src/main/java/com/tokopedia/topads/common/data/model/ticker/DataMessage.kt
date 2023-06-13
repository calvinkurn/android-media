package com.tokopedia.topads.common.data.model.ticker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataMessage (
    @SerializedName("message")
    @Expose
    val message: List<String> = arrayListOf(),

    @SerializedName("ticker_info")
    @Expose
    val tickerInfo: TickerInfo = TickerInfo()
)


data class TickerInfo (
    @SerializedName("ticker_message")
    @Expose
    val tickerMessage: String = "",

    @SerializedName("ticker_type")
    @Expose
    val tickerType: String = ""
)
