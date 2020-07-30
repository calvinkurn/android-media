package com.tokopedia.topads.data.response

import com.google.gson.annotations.SerializedName

data class ResponseKeywordSuggestion(
        @SerializedName("data")
        val result: Result = Result()
) {
    data class Result(
            @field:SerializedName("topAdsGetKeywordSuggestionV3")
            val topAdsGetKeywordSuggestionV3: TopAdsGetKeywordSuggestionV3 = TopAdsGetKeywordSuggestionV3()
    )

    data class TopAdsGetKeywordSuggestionV3(

            @field:SerializedName("data")
            val data: List<KeywordData> = listOf(),

            @field:SerializedName("errors")
            val errors: List<Error> = listOf()
    )

    data class KeywordData(

            @field:SerializedName("product_id")
            val productId: Int = 0,

            @field:SerializedName("keyword_data")
            val keywordData: List<KeywordDataItem> = listOf(),

            @field:SerializedName("min_bid")
            val minBid: Int = 0
    )

    data class KeywordDataItem(
            @field:SerializedName("bid_suggest")
            val bidSuggest: Int = 0,

            @field:SerializedName("total_search")
            val totalSearch: String = "",

            @field:SerializedName("keyword")
            val keyword: String = "",

            @field:SerializedName("source")
            val source: String = ""
    )

}