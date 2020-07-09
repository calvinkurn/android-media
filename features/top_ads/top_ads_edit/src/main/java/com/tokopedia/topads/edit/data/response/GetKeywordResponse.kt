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

    data class KeywordsItem(

            @field:SerializedName("type")
            var type: Int = 11,

            @field:SerializedName("status")
            var status: Int = 1,

            @field:SerializedName("keyword_id")
            val keywordId: String = "",

            @field:SerializedName("price_bid")
            var priceBid: Int = 0,

            @field:SerializedName("isChecked")
            var isChecked: Boolean = false,

            @field:SerializedName("tag")
            val tag: String = "",
            @field:SerializedName("source")
            val source: String = "") : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readByte() != 0.toByte(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(type)
            parcel.writeInt(status)
            parcel.writeString(keywordId)
            parcel.writeInt(priceBid)
            parcel.writeByte(if (isChecked) 1 else 0)
            parcel.writeString(tag)
            parcel.writeString(source)
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

