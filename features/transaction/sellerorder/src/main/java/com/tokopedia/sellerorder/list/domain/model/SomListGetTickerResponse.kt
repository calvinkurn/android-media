package com.tokopedia.sellerorder.list.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListGetTickerResponse(
        @SerializedName("data")
        @Expose
        val `data`: Data = Data()
) {
    data class Data(
            @SerializedName("orderTickers")
            @Expose
            val orderTickers: OrderTickers = OrderTickers()
    ) {
        data class OrderTickers(
                @SerializedName("tickers")
                @Expose
                val tickers: List<Ticker> = listOf()
        ) {
            data class Ticker(
                    @SerializedName("body")
                    @Expose
                    val body: String = "",
                    @SerializedName("id")
                    @Expose
                    val id: Int = 0,
                    @SerializedName("is_active")
                    @Expose
                    val isActive: Boolean = false,
                    @SerializedName("short_desc")
                    @Expose
                    val shortDesc: String = ""
            )
        }
    }
}