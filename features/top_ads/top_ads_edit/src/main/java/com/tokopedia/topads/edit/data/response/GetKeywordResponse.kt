package com.tokopedia.topads.edit.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class GetKeywordResponse(

        @field:SerializedName("topAdsListKeyword")
        val topAdsListKeyword: TopAdsListKeyword = TopAdsListKeyword()
) {
    data class TopAdsListKeyword(

            @field:SerializedName("data")
            val data: Data = Data(),

            @field:SerializedName("error")
            val error: Error = Error()
    )

    data class Data(

            @field:SerializedName("pagination")
            val pagination: Pagination = Pagination(),

            @field:SerializedName("keywords")
            val keywords: List<KeywordsItem> = listOf()
    )

    data class Error(

            @field:SerializedName("code")
            val code: String = "",

            @field:SerializedName("detail")
            val detail: String = "",

            @field:SerializedName("title")
            val title: String? = null
    )


    data class Pagination(

            @field:SerializedName("next_cursor")
            val nextCursor: String? = null
    )

    data class KeywordsItem(

            @field:SerializedName("typeKeyword")
            var typeKeyword: String = "positive_phrase",

            @field:SerializedName("keyword_id")
            val keywordId: String = "",

            @field:SerializedName("price_bid")
            var priceBid: Int = 0,

            @field:SerializedName("tag")
            val tag: String = "") : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readString()

        )


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(typeKeyword)
            parcel.writeString(keywordId)
            parcel.writeString(tag)
            parcel.writeInt(priceBid)

        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<KeywordsItem> {
            override fun createFromParcel(parcel: Parcel): KeywordsItem {
                return KeywordsItem(parcel)
            }

            override fun newArray(size: Int): Array<KeywordsItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}

