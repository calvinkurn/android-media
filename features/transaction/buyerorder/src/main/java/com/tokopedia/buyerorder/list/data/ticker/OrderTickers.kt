package com.tokopedia.buyerorder.list.data.ticker

import com.google.gson.annotations.SerializedName

data class OrderTickers(

        @field:SerializedName("request_by")
        val requestBy: String? = null,

        @field:SerializedName("total")
        val total: Int,

        @field:SerializedName("user_id")
        val userId: String? = null,

        @field:SerializedName("client")
        val client: String? = null,

        @field:SerializedName("tickers")
        val tickers: List<TickersItem?>? = null
)