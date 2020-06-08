package com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopOperationalHourStatus(
        @SerializedName("timestamp")
        @Expose
        val timestamp: String = "",
        @SerializedName("statusActive")
        @Expose
        val statusActive: Boolean = false,
        @SerializedName("startTime")
        @Expose
        val startTime: String = "",
        @SerializedName("endTime")
        @Expose
        val endTime: String = "",
        @SerializedName("tickerTitle")
        @Expose
        val tickerTitle: String = "",
        @SerializedName("tickerMessage")
        @Expose
        val tickerMessage: String = "",
        @SerializedName("error")
        @Expose
        val error: Error = Error()
){
    data class Response(
        @SerializedName("getShopOperationalHourStatus")
        @Expose
        val shopOperationalHourStatus: ShopOperationalHourStatus
    )

    data class Error(
            @SerializedName("message")
            @Expose
            val message: String = ""
    )
}