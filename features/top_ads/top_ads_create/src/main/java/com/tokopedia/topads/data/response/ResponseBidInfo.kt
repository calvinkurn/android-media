package com.tokopedia.topads.data.response

import com.google.gson.annotations.SerializedName

data class ResponseBidInfo(
        @SerializedName("data")
        val result: Result = Result()
) {
    data class Result(
            @field:SerializedName("topadsBidInfo")
            val topadsBidInfo: TopadsBidInfo = TopadsBidInfo()
    )
}

data class TopadsBidInfo(
        @field:SerializedName("data")
        val data: List<BidInfoDataItem> = listOf(),

        @field:SerializedName("request_type")
        val requestType: String = ""

)
data class BidInfoDataItem(
        @field:SerializedName("suggestion_bid")
        var suggestionBid: Int = 0,

        @field:SerializedName("max_bid")
        val maxBid: Int = 0,

        @field:SerializedName("min_bid")
        val minBid: Int = 0,

        var keywordType: String = "Spesifik"

)



