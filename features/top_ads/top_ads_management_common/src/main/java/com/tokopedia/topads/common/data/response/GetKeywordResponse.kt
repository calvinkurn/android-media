package com.tokopedia.topads.common.data.response

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
            @SerializedName("type")
            var type: Int = 11,
            @SerializedName("status")
            var status: Int = 1,
            @SerializedName("keyword_id")
            val keywordId: String = "",
            @SerializedName("price_bid")
            var priceBid: String = "0",
            @SerializedName("isChecked")
            var isChecked: Boolean = false,
            @SerializedName("tag")
            val tag: String = "",
            @SerializedName("source")
            val source: String = "",
            @SerializedName("create_by")
            var createBy: String = "",
            @SerializedName("create_time_utc")
            var createTimeUtc: String = "",
            @SerializedName("group_id")
            var groupId: String = "",
            @SerializedName("shop_id")
            var shopId: String = "",
            @SerializedName("update_by")
            var updateBy: String = "",
            @SerializedName("update_time_utc")
            var updateTimeUtc: String = "") : Parcelable
}

