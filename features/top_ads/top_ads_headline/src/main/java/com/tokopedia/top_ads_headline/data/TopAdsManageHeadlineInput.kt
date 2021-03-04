package com.tokopedia.top_ads_headline.data


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class TopAdsManageHeadlineInput(
        @SerializedName("operation")
        var operation: Operation = Operation(),
        @SerializedName("source")
        var source: String = ""
) {
    data class Operation(
            @SerializedName("action")
            var action: String = "",
            @SerializedName("group")
            var group: Group = Group()
    ) {
        data class Group(
                @SerializedName("adOperations")
                var adOperations: List<AdOperation> = ArrayList(),
                @SerializedName("dailyBudget")
                var dailyBudget: Float = 0.0F,
                @SerializedName("id")
                var id: String = "",
                @SerializedName("keywordOperations")
                var keywordOperations: List<KeywordOperation> = ArrayList(),
                @SerializedName("name")
                var name: String = "",
                @SerializedName("priceBid")
                var priceBid: Float = 0.0F,
                @SerializedName("scheduleEnd")
                var scheduleEnd: String = "",
                @SerializedName("scheduleStart")
                var scheduleStart: String = "",
                @SerializedName("shopID")
                var shopID: String = "",
                @SerializedName("status")
                var status: String = ""
        ) {
            @Parcelize
            data class AdOperation(
                    @SerializedName("action")
                    var action: String = "",
                    @SerializedName("ad")
                    var ad: Ad = Ad()
            ) : Parcelable {
                @Parcelize
                data class Ad(
                        @SerializedName("id")
                        var id: String = "",
                        @SerializedName("productIDs")
                        var productIDs: List<String> = ArrayList(),
                        @SerializedName("slogan")
                        var slogan: String = "",
                        @SerializedName("title")
                        var title: String = ""
                ) : Parcelable
            }

            @Parcelize
            data class KeywordOperation(
                    @SerializedName("action")
                    var action: String = "",
                    @SerializedName("keyword")
                    var keyword: Keyword = Keyword()
            ) : Parcelable {
                @Parcelize
                data class Keyword(
                        @SerializedName("id")
                        var id: String = "0",
                        @SerializedName("priceBid")
                        var priceBid: Long = 0,
                        @SerializedName("status")
                        var status: String = "",
                        @SerializedName("tag")
                        var tag: String = "",
                        @SerializedName("type")
                        var type: String = ""
                ) : Parcelable
            }
        }
    }
}