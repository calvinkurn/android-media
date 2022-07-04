package com.tokopedia.shop.common.data.source.cloud.model


import com.google.gson.annotations.SerializedName

data class MaxStockThresholdResponse(
    @SerializedName("GetIMSMeta")
    val getIMSMeta: GetIMSMeta
) {
    data class GetIMSMeta(
        @SerializedName("data")
        val data: Data,
        @SerializedName("header")
        val header: Header
    ) {
        data class Data(
            @SerializedName("max_stock_threshold")
            val maxStockThreshold: String
        )
        data class Header(
            @SerializedName("error_code")
            val errorCode: String,
            @SerializedName("messages")
            val messages: List<Any>,
            @SerializedName("process_time")
            val processTime: Double,
            @SerializedName("reason")
            val reason: String
        )
    }
}