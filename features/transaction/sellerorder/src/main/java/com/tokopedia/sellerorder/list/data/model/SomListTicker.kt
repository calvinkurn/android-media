package com.tokopedia.sellerorder.list.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-08-27.
 */
data class SomListTicker (
    @SerializedName("data")
    @Expose
    val data: Data = Data()
) {
    data class Data (
         @SerializedName("orderTickers")
         @Expose
         val orderTickers: OrderTickers = OrderTickers()
    ) {
        data class OrderTickers (
            @SerializedName("user_id")
            @Expose
            val userId: String = "",

            @SerializedName("request_by")
            @Expose
            val requestBy: String = "",

            @SerializedName("client")
            @Expose
            val client: String = "",

            @SerializedName("total")
            @Expose
            val total: Int = 0,

            @SerializedName("tickers")
            @Expose
            val listTicker: List<Tickers> = listOf()
        ) {
            data class Tickers(
                    @SerializedName("id")
                    @Expose
                    val tickerId: Int = 0,

                    @SerializedName("body")
                    @Expose
                    val body: String = "",

                    @SerializedName("short_desc")
                    @Expose
                    val shortDesc: String = "",

                    @SerializedName("is_active")
                    @Expose
                    val isActive: Boolean = false
            )
        }
    }
}