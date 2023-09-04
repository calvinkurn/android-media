package com.tokopedia.topads.data


import com.google.gson.annotations.SerializedName

data class TopAdsGetBidSuggestionResponse(
    @SerializedName("topAdsGetBidSuggestionByProductIDs")
    val topAdsGetBidSuggestionByProductIDs: TopAdsGetBidSuggestionByProductIDs
) {
    data class TopAdsGetBidSuggestionByProductIDs(
        @SerializedName("data")
        val bidData: BidData,
        @SerializedName("error")
        val error: Error
    ) {
        data class BidData(
            @SerializedName("bidSuggestion")
            val bidSuggestion: Int
        )

        data class Error(
            @SerializedName("code")
            val code: String,
            @SerializedName("detail")
            val detail: String,
            @SerializedName("title")
            val title: String
        )
    }
}
