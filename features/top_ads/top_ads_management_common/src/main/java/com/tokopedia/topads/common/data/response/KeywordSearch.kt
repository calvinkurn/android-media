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
        val bidSuggest: String = "0",
        @SerializedName("competition")
        var competition: String? = "",
        @SerializedName("keyword")
        var keyword: String? = "",
        @SerializedName("min_bid")
        val minBid: String = " 0",
        @SerializedName("product_id")
        val productId: String = "0",
        @SerializedName("source")
        val source: String? = "",
        @SerializedName("total_search")
        var totalSearch: Int = 0,
        var onChecked: Boolean = false
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString().toString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString().toString(),
                parcel.readString().toString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readByte() != 0.toByte()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(bidSuggest)
            parcel.writeString(competition)
            parcel.writeString(keyword)
            parcel.writeString(minBid)
            parcel.writeString(productId)
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