package com.tokopedia.buyerorder.list.data.ticker

import com.google.gson.annotations.SerializedName

data class TickerResponse(

        @field:SerializedName("orderTickers")
        val orderTickers: OrderTickers? = null
)