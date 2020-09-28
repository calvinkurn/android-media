package com.tokopedia.topads.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ResponseKeywordSuggestion(
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

        @field:SerializedName("competition")
        val competition: String = "",

        @field:SerializedName("source")
        val source: String = "",
        var onChecked:Boolean = false,
        var fromSearch:Boolean = false
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString().toString(),
                parcel.readString().toString(),
                parcel.readString().toString(),
                parcel.readString().toString(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(bidSuggest)
                parcel.writeString(totalSearch)
                parcel.writeString(keyword)
                parcel.writeString(competition)
                parcel.writeString(source)
                parcel.writeByte(if (onChecked) 1 else 0)
                parcel.writeByte(if (onChecked) 1 else 0)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<KeywordDataItem> {
                override fun createFromParcel(parcel: Parcel): KeywordDataItem {
                        return KeywordDataItem(parcel)
                }

                override fun newArray(size: Int): Array<KeywordDataItem?> {
                        return arrayOfNulls(size)
                }
        }
}

