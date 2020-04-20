package com.tokopedia.topads.common.data.response


import android.os.Parcel
import android.os.Parcelable
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
            ) : Parcelable{
                constructor(parcel: Parcel) : this(
                        parcel.readInt(),
                        parcel.readString(),
                        parcel.readString()) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(bidSuggest)
                    parcel.writeString(keyword)
                    parcel.writeString(totalSearch)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<Data> {
                    override fun createFromParcel(parcel: Parcel): Data {
                        return Data(parcel)
                    }

                    override fun newArray(size: Int): Array<Data?> {
                        return arrayOfNulls(size)
                    }
                }

            }
        }
    }
}
