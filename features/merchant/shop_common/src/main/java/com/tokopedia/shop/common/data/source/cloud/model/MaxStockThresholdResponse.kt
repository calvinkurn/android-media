package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class MaxStockThresholdResponse(
    @Expose
    @SerializedName("GetIMSMeta")
    val getIMSMeta: GetIMSMeta
) {
    data class GetIMSMeta(
        @Expose
        @SerializedName("data")
        val data: Data,
        @Expose
        @SerializedName("header")
        val header: Header
    ) {
        data class Data(
            @Expose
            @SerializedName("max_stock_threshold")
            val maxStockThreshold: String
        )
    }
}