package com.tokopedia.topads.dashboard.data.model


import com.google.gson.annotations.SerializedName

data class TopAdsDeletedAdsResponse(
    @SerializedName("topadsDeletedAds")
    val topAdsDeletedAds: TopadsDeletedAds
) {
    data class TopadsDeletedAds(
        @SerializedName("data")
        val topAdsDeletedAdsItemList: List<TopAdsDeletedAdsItem>,
        @SerializedName("page")
        val page: Page
    ) {
        data class TopAdsDeletedAdsItem(
            @SerializedName("ad_deleted_time")
            val adDeletedTime: String,
            @SerializedName("ad_title")
            val adTitle: String,
            @SerializedName("product_image_uri")
            val productImageUri: String,
            @SerializedName("stat_avg_click")
            val statAvgClick: String,
            @SerializedName("stat_total_click")
            val statTotalClick: String,
            @SerializedName("stat_total_conversion")
            val statTotalConversion: String,
            @SerializedName("stat_total_ctr")
            val statTotalCtr: String,
            @SerializedName("stat_total_gross_profit")
            val statTotalGrossProfit: String,
            @SerializedName("stat_total_impression")
            val statTotalImpression: String,
            @SerializedName("stat_total_roas")
            val statTotalRoas: String,
            @SerializedName("stat_total_sold")
            val statTotalSold: String,
            @SerializedName("stat_total_spent")
            val statTotalSpent: String,
            @SerializedName("stat_total_top_slot_impression")
            val statTotalTopSlotImpression: String
        )

        data class Page(
            @SerializedName("per_page")
            val perPage: Int,
            @SerializedName("total")
            val total: Int
        )
    }
}