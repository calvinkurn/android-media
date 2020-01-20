package com.tokopedia.topads.data.response


import com.google.gson.annotations.SerializedName

data class ResponseKeywordSuggestion(

        @SerializedName("data")
        val result: Result = Result()
) {
    data class Result(
            @SerializedName("topAdsGetKeywordSuggestionV2")
            val topAdsGetKeywordSuggestion: TopAdsGetKeywordSuggestion = TopAdsGetKeywordSuggestion()
    ) {
        data class TopAdsGetKeywordSuggestion(
                @SerializedName("data")
                val `data`: List<Data> = listOf(),
                @SerializedName("errors")
                val errors: List<Error> = listOf()
        ) {
            data class Data(
                    @SerializedName("bid_suggest")
                    val bidSuggest: Int = 0,
                    @SerializedName("keyword")
                    val keyword: String = "",
                    @SerializedName("total_search")
                    val totalSearch: String = ""
            )
        }
    }
}
