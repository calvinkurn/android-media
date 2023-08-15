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
        val nextCursor: String = ""
    ) {
        data class AdGroup(
            @SerializedName("adGroupID")
            val adGroupID: String = "",
            @SerializedName("adGroupType")
            val adGroupType: String = "",
            @SerializedName("adGroupName")
            val adGroupName: String = "",
            @SerializedName("count")
            val count: Int = 0
        )
    }

    fun toInsightUiModel(insightType: Int): InsightUiModel {
        return InsightUiModel(
            adGroups = toInsightListUiModel(this.topAdsListAllInsightCounts.adGroups, insightType).toMutableList(),
            insightType = insightType,
            nextCursor = this.topAdsListAllInsightCounts.nextCursor
        )
    }

    fun toInsightUiAtHomeModel(insightType: Int): List<InsightListUiModel> {
        return topAdsListAllInsightCounts.adGroups.map {
            AdGroupUiModel(
                it.adGroupID,
                it.adGroupName,
                it.adGroupType,
                it.count,
                insightType = insightType,
                showGroupType = true
            )
        }
    }

    private fun toInsightListUiModel(adGroups: List<TopAdsListAllInsightCounts.AdGroup>, insightType: Int): List<InsightListUiModel> {
        return adGroups.map {
            AdGroupUiModel(
                it.adGroupID,
                it.adGroupName,
                it.adGroupType,
                it.count,
                insightType = insightType

            )
        }
    }
}
