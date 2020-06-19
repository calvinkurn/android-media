package com.tokopedia.topads.edit.data.response

import com.google.gson.annotations.SerializedName

data class ResponseBidInfo(
        @SerializedName("data")
        val result: Result = Result()
) {
    data class Result(
            @field:SerializedName("topadsBidInfo")
            val topadsBidInfo: TopadsBidInfo = TopadsBidInfo()
    ) {
        data class TopadsBidInfo(
                @field:SerializedName("data")
                val data: List<DataItem> = listOf(),

                @field:SerializedName("request_type")
                val requestType: String = ""

        ) {
            data class DataItem(

                    @field:SerializedName("id")
                    val adId: Int = 0,

                    @field:SerializedName("suggestion_bid")
                    val suggestionBid: Int = 0,

                    @field:SerializedName("max_bid")
                    val maxBid: Int = 0,

                    @field:SerializedName("min_bid")
                    val minBid: Int = 0
            )
        }
    }
}
