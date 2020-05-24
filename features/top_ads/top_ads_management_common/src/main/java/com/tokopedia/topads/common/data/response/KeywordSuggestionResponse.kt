package com.tokopedia.topads.common.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

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
            data class DataItem(

                    @field:SerializedName("product_id")
                    val productId: Int = 0,

                    @field:SerializedName("keyword_data")
                    val keywordData: List<KeywordDataItem> = listOf(),

                    @field:SerializedName("min_bid")
                    val minBid: Int = 0


            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                        parcel.readInt(),
                        parcel.createTypedArrayList(KeywordDataItem),
                        parcel.readInt()) {
                }

                data class KeywordDataItem(
                        @field:SerializedName("bid_suggest")
                        val bidSuggest: Int = 0,

                        @field:SerializedName("total_search")
                        val totalSearch: String = "",

                        @field:SerializedName("keyword")
                        val keyword: String = "",

                        @field:SerializedName("source")
                        val source: String = ""
                ) : Parcelable {
                    constructor(parcel: Parcel) : this(
                            parcel.readInt(),
                            parcel.readString(),
                            parcel.readString(),
                            parcel.readString()) {
                    }

                    override fun writeToParcel(parcel: Parcel, flags: Int) {
                        parcel.writeInt(bidSuggest)
                        parcel.writeString(totalSearch)
                        parcel.writeString(keyword)
                        parcel.writeString(source)
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

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(productId)
                    parcel.writeTypedList(keywordData)
                    parcel.writeInt(minBid)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<DataItem> {
                    override fun createFromParcel(parcel: Parcel): DataItem {
                        return DataItem(parcel)
                    }

                    override fun newArray(size: Int): Array<DataItem?> {
                        return arrayOfNulls(size)
                    }
                }
            }
        }
    }
}