package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class MaxStockThresholdResponse(
    @SerializedName("GetIMSMeta")
    val getIMSMeta: GetIMSMeta
) {

    fun getMaxStockFromResponse(): Int? {
        return getIMSMeta.data.maxStockThreshold.takeIf { it.isNotEmpty() }?.toInt()
    }

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
    }
}