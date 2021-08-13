package com.tokopedia.product.detail.data.model.ticker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GeneralTickerDataModel(
        @SerializedName("tickers")
        @Expose
        val tickerDataModels: List<TickerDetailDataModel> = ArrayList()
) {

    data class TickerResponse(
            @SerializedName("ticker")
            @Expose
            val response: GeneralTickerDataModel = GeneralTickerDataModel()
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