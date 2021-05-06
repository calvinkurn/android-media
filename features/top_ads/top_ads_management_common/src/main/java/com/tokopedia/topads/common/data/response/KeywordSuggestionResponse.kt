package com.tokopedia.topads.common.data.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class KeywordSuggestionResponse(
        @SerializedName("data")
        val result: Result = Result()
) {
    data class Result(
            @field:SerializedName("topAdsGetKeywordSuggestionV3")
            val topAdsGetKeywordSuggestionV3: TopAdsGetKeywordSuggestionV3 = TopAdsGetKeywordSuggestionV3()
    )
}

data class TopAdsGetKeywordSuggestionV3(

        @field:SerializedName("data")
        val data: List<KeywordData> = listOf(),

        @field:SerializedName("errors")
        val errors: List<Error> = listOf()
)

@Parcelize
data class KeywordData(

        @field:SerializedName("product_id")
        val productId: String = "0",

        @field:SerializedName("keyword_data")
        val keywordData: List<KeywordDataItem> = listOf(),

        @field:SerializedName("min_bid")
        val minBid: String = "0"


) : Parcelable

@Parcelize
data class KeywordDataItem(
        @field:SerializedName("bid_suggest")
        var bidSuggest: String = "0",

        @field:SerializedName("total_search")
        var totalSearch: String = "",

        @field:SerializedName("keyword")
        var keyword: String = "",

        @field:SerializedName("source")
        val source: String = "",

        @field:SerializedName("competition")
        val competition: String = "",

        var onChecked: Boolean = false,

        var fromSearch:Boolean = false,

        @Expose(serialize = false, deserialize = false)
        var keywordType: String = "Spesifik"


) : Parcelable
