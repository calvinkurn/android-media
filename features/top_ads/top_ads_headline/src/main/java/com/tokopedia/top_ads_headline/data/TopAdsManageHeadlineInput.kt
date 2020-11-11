package com.tokopedia.top_ads_headline.data


import com.google.gson.annotations.SerializedName

data class TopAdsManageHeadlineInput(
        @SerializedName("operation")
        var operation: Operation,
        @SerializedName("source")
        var source: String
) {
    data class Operation(
            @SerializedName("action")
            var action: String,
            @SerializedName("group")
            var group: Group
    ) {
        data class Group(
                @SerializedName("adOperations")
                var adOperations: List<AdOperation>,
                @SerializedName("dailyBudget")
                var dailyBudget: Int,
                @SerializedName("id")
                var id: String,
                @SerializedName("keywordOperations")
                var keywordOperations: List<KeywordOperation>,
                @SerializedName("name")
                var name: String,
                @SerializedName("priceBid")
                var priceBid: Int,
                @SerializedName("scheduleEnd")
                var scheduleEnd: String,
                @SerializedName("scheduleStart")
                var scheduleStart: String,
                @SerializedName("shopID")
                var shopID: String
        ) {
            data class AdOperation(
                    @SerializedName("action")
                    var action: String,
                    @SerializedName("ad")
                    var ad: Ad
            ) {
                data class Ad(
                        @SerializedName("id")
                        var id: String,
                        @SerializedName("productIDs")
                        var productIDs: List<String>,
                        @SerializedName("slogan")
                        var slogan: String,
                        @SerializedName("title")
                        var title: String
                )
            }

            data class KeywordOperation(
                    @SerializedName("action")
                    var action: String,
                    @SerializedName("keyword")
                    var keyword: Keyword
            ) {
                data class Keyword(
                        @SerializedName("priceBid")
                        var priceBid: Int,
                        @SerializedName("status")
                        var status: String,
                        @SerializedName("tag")
                        var tag: String,
                        @SerializedName("type")
                        var type: String
                )
            }
        }
    }
}