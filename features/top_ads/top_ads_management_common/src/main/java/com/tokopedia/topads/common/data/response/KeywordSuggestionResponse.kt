package com.tokopedia.topads.common.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class KeywordSuggestionResponse(
        @SerializedName("data")
        val result: Result = Result()
) {
    data class Result(
            @field:SerializedName("topAdsGetKeywordSuggestionV3")
            val topAdsGetKeywordSuggestionV3: TopAdsGetKeywordSuggestionV3 = TopAdsGetKeywordSuggestionV3()
    ) {
        data class TopAdsGetKeywordSuggestionV3(

                @field:SerializedName("data")
                val data: List<DataItem> = listOf(),

                @field:SerializedName("errors")
                val errors: List<Error> = listOf()
        ) {
            @Parcelize
            data class DataItem(

                    @field:SerializedName("product_id")
                    val productId: Int = 0,

                    @field:SerializedName("keyword_data")
                    val keywordData: List<KeywordDataItem> = listOf(),

                    @field:SerializedName("min_bid")
                    val minBid: Int = 0


            ) : Parcelable {

                @Parcelize
                data class KeywordDataItem(
                        @field:SerializedName("bid_suggest")
                        val bidSuggest: Int = 0,

                        @field:SerializedName("total_search")
                        val totalSearch: String = "",

                        @field:SerializedName("keyword")
                        val keyword: String = "",

                        @field:SerializedName("source")
                        val source: String = ""
                ) : Parcelable

            }
        }
    }
}