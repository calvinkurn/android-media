package com.tokopedia.stickylogin.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StickyLoginTickerPojo(
    @SerializedName("tickers")
    @Expose
    val tickers: List<TickerDetail> = ArrayList()
) {
    data class TickerResponse(
        @SerializedName("ticker")
        @Expose
        val response: StickyLoginTickerPojo = StickyLoginTickerPojo()
    )

    data class TickerDetail(
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("layout")
        @Expose
        val layout: String = ""
    )
}