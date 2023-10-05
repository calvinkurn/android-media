package com.tokopedia.topads.dashboard.recommendation.data.model.cloud

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class TopAdsTotalAdGroupsWithInsightResponse(
    @SerializedName("topAdsGetTotalAdGroupsWithInsightByShopID")
    val topAdsGetTotalAdGroupsWithInsightByShopID: TopAdsGetTotalAdGroupsWithInsightByShopID = TopAdsGetTotalAdGroupsWithInsightByShopID()
) {
    data class TopAdsGetTotalAdGroupsWithInsightByShopID(
        @SerializedName("data")
        val totalAdGroupsWithInsight: TotalAdGroupsWithInsight = TotalAdGroupsWithInsight(),
        @SerializedName("error")
        val error: Error = Error()
    ) {
        data class TotalAdGroupsWithInsight(
            @SerializedName("totalAdGroups")
            val totalAdGroups: Int = 0,
            @SerializedName("totalAdGroupsWithInsight")
            val totalAdGroupsWithInsight: Int = 0
        )
    }
}
