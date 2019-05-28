package com.tokopedia.topads.auto.data.network.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.auto.data.entity.BidInfoData

/**
 * Author errysuprayogi on 15,May,2019
 */
data class TopadsBidInfo(
        @SerializedName("request_type")
        val requestType: String = "",
        @SerializedName("data")
        val data: List<BidInfoData> = listOf(BidInfoData()),
        @SerializedName("errors")
        val error: List<ErrorResponse> = listOf(ErrorResponse())
) {
    data class Response(
            @SerializedName("topadsBidInfo")
            val bidInfo: TopadsBidInfo
    )
}