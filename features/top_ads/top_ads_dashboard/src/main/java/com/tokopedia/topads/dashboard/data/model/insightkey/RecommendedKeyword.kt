package com.tokopedia.topads.dashboard.data.model.insightkey

import com.google.gson.annotations.SerializedName

data class RecommendedKeyword(
    @SerializedName("shop_id")
    var shopId: Int = 0,
    @SerializedName("recommended_keyword_count")
    var keywordCount: Int = 0,
    @SerializedName("group_count")
    var groupCount: Int = 0,
    @SerializedName("total_impression_count")
    var totalImpressionCount: Int = 0,
    @SerializedName("recommended_keyword_details")
    var keywordDetails: List<RecommendedKeywordDetail> = listOf()
)

data class RecommendedKeywordDetail(
    @SerializedName("keyword_tag")
    var keywordTag: String = "",
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("group_name")
    var groupName: String = "",
    @SerializedName("total_hits")
    var totalHits: Int = 0,
    @SerializedName("recommended_bid")
    var recommendedBid: Double = 0.0,
    @SerializedName("min_bid")
    var minBid: Int = 0,
    @SerializedName("max_bid")
    var maxBid: Int = 0,
    @SerializedName("impression_count")
    var impressionCount: Int = 0
)