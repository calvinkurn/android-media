package com.tokopedia.shop.info.data.response

import com.google.gson.annotations.SerializedName

data class GetShopStatsRawDataResponse(
    @SerializedName("getShopStatsRawData")
    val getShopStatsRawData: GetShopStatsRawData
) {
    data class GetShopStatsRawData(
        @SerializedName("error")
        val error: Error,
        @SerializedName("result")
        val result: Result
    ) {
        data class Error(
            @SerializedName("message")
            val message: String
        )

        data class Result(
            @SerializedName("chatAndDiscussionReplySpeed")
            val chatAndDiscussionReplySpeed: Float
        )
    }
}
