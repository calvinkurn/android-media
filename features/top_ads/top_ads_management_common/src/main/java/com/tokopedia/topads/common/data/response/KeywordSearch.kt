package com.tokopedia.topads.common.data.response


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class KeywordSearch(
    @SerializedName("topAdsKeywordSearchTerm")
    val topAdsKeywordSearchTerm: TopAdsKeywordSearchTerm = TopAdsKeywordSearchTerm()
)
data class TopAdsKeywordSearchTerm(
        @SerializedName("data")
        val `data`: List<SearchData> = listOf(),
        @SerializedName("errors")
        val errors: List<Any> = listOf()
)
data class SearchData(
        @SerializedName("bid_suggest")
        val bidSuggest: Int = 0,
        @SerializedName("competition")
        var competition: String? = "",
        @SerializedName("keyword")
        var keyword: String? = "",
        @SerializedName("max_win_bid")
        val maxWinBid: Int = 0,
        @SerializedName("min_bid")
        val minBid: Int = 0,
        @SerializedName("min_win_bid")
        val minWinBid: Int = 0,
        @SerializedName("product_id")
        val productId: Int = 0,
        @SerializedName("source")
        val source: String? = "",
        @SerializedName("total_search")
        var totalSearch: Int = 0,
        var onChecked:Boolean = false
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readByte() != 0.toByte()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(bidSuggest)
                parcel.writeString(competition)
                parcel.writeString(keyword)
                parcel.writeInt(maxWinBid)
                parcel.writeInt(minBid)
                parcel.writeInt(minWinBid)
                parcel.writeInt(productId)
                parcel.writeString(source)
                parcel.writeInt(totalSearch)
                parcel.writeByte(if (onChecked) 1 else 0)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<SearchData> {
                override fun createFromParcel(parcel: Parcel): SearchData {
                        return SearchData(parcel)
                }

                override fun newArray(size: Int): Array<SearchData?> {
                        return arrayOfNulls(size)
                }
        }
}