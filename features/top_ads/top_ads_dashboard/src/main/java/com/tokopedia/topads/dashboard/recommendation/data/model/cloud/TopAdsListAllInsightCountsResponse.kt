package com.tokopedia.topads.dashboard.recommendation.data.model.cloud

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightUiModel

data class TopAdsListAllInsightCountsResponse(
    @SerializedName("topAdsListAllInsightCounts")
    val topAdsListAllInsightCounts: TopAdsListAllInsightCounts = TopAdsListAllInsightCounts()
) {
    data class TopAdsListAllInsightCounts(
        @SerializedName("adGroups")
        val adGroups: List<AdGroup> = listOf(),
        @SerializedName("error")
        val error: Error = Error(),
        @SerializedName("nextCursor")
        val nextCursor: String = "",
    ) {
        data class AdGroup(
            @SerializedName("adGroupID")
            val adGroupID: String = "",
            @SerializedName("adGroupType")
            val adGroupType: String = "",
            @SerializedName("count")
            val count: Int = 0
        )
    }

    fun toInsightUiModel(): InsightUiModel {
        return InsightUiModel(
            adGroups = toInsightListUiModel(this.topAdsListAllInsightCounts.adGroups).toMutableList(),
            insightType = 0,
            nextCursor = this.topAdsListAllInsightCounts.nextCursor
        )
    }

    private fun toInsightListUiModel(adGroups: List<TopAdsListAllInsightCounts.AdGroup>): List<InsightListUiModel> {
        return adGroups.map { AdGroupUiModel(it.adGroupID, it.adGroupType, it.count) }

    }
}
