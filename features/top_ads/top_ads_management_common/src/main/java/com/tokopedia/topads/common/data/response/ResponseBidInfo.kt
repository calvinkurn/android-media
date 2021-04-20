package com.tokopedia.topads.common.data.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponseBidInfo(
        @SerializedName("data")
        val result: Result = Result()
) {
    data class Result(
            @field:SerializedName("topadsBidInfo")
            val topadsBidInfo: TopadsBidInfo = TopadsBidInfo()
    )
}

data class TopadsBidInfo(
        @field:SerializedName("data")
        val data: List<DataItem> = listOf(),

        @field:SerializedName("request_type")
        val requestType: String = ""

) {
    @Parcelize
    data class DataItem(

            @field:SerializedName("id")
            var adId: String = "0",
            @field:SerializedName("suggestion_bid")
            var suggestionBid: String = "0",
            @field:SerializedName("max_bid")
            var maxBid: String = "0",
            @field:SerializedName("min_bid")
            var minBid: String = "0",
            @SerializedName("estimation")
            var estimation: Estimation = Estimation(),
            @SerializedName("max_bid_fmt")
            var maxBidFmt: String = "",
            @SerializedName("max_daily_budget")
            var maxDailyBudget: Int = 0,
            @SerializedName("max_daily_budget_fmt")
            var maxDailyBudgetFmt: String = "",
            @SerializedName("min_bid_fmt")
            var minBidFmt: String = "",
            @SerializedName("min_daily_budget")
            var minDailyBudget: Int = 0,
            @SerializedName("min_daily_budget_fmt")
            var minDailyBudgetFmt: String = "",
            @SerializedName("multiplier")
            var multiplier: Int = 0,
            @SerializedName("suggestion_bid_fmt")
            var suggestionBidFmt: String = "",
            @Expose(serialize = false, deserialize = false)
            var shopStatus: Int = 0,
            @Expose(serialize = false, deserialize = false)
            var keywordType: String = "Spesifik"
    ) : Parcelable {
        @Parcelize
        data class Estimation(
                @SerializedName("max_bid")
                var maxBid: String = "0",
                @SerializedName("max_bid_fmt")
                var maxBidFmt: String = "",
                @SerializedName("min_bid")
                var minBid: String = "0",
                @SerializedName("min_bid_fmt")
                var minBidFmt: String = ""
        ) : Parcelable
    }
}
