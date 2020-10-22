package com.tokopedia.topads.edit.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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
            val keywords: List<KeywordsItem> = listOf(),

            @field:SerializedName("pagination")
            val pagination: Pagination = Pagination()
    )

    data class Pagination(

            @field:SerializedName("next_cursor")
            val cursor: String = ""

    )

    data class Error(

            @field:SerializedName("code")
            val code: String = "",

            @field:SerializedName("detail")
            val detail: String = "",

            @field:SerializedName("title")
            val title: String? = null
    )

    @Parcelize
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
            val source: String = "") : Parcelable
}

