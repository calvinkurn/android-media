package com.tokopedia.topads.dashboard.recommendation.data.mapper

import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_CHIPS
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_INSIGHT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PERFORMANCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsListAllInsightCountsResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.*
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.ChipsData.chipsList
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.EmptyStateData
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.views.fragments.findPositionOfSelected
import javax.inject.Inject

class GroupDetailMapper @Inject constructor() {
    val detailPageDataMap: MutableMap<Int, GroupDetailDataModel> = mutableMapOf(
        TYPE_INSIGHT to InsightTypeChipsUiModel(),
        TYPE_PERFORMANCE to GroupPerformanceWidgetUiModel(),
        TYPE_CHIPS to GroupDetailChipsUiModel(),
        TYPE_POSITIVE_KEYWORD to GroupInsightsUiModel(),
        TYPE_KEYWORD_BID to GroupInsightsUiModel(),
        TYPE_GROUP_BID to GroupInsightsUiModel(),
        TYPE_DAILY_BUDGET to GroupInsightsUiModel(),
        TYPE_NEGATIVE_KEYWORD_BID to GroupInsightsUiModel()
    )

    fun reArrangedDataMap(): MutableMap<Int, GroupDetailDataModel> {
        val map = mutableMapOf<Int, GroupDetailDataModel>()
        val position = chipsList.findPositionOfSelected { it.isSelected }
        return if (position == TYPE_INSIGHT) {
//            detailPageDataMap.remove(8)
            detailPageDataMap
            var isPresent = false
            detailPageDataMap.values.forEach {
                if ((it as? GroupInsightsUiModel)?.isAvailable() == true) isPresent = true
            }
            if (!isPresent) {

                //handle and show other groups here
                val adGroups =
                    (detailPageDataMap[TYPE_INSIGHT] as? InsightTypeChipsUiModel)?.adGroupList
                        ?: mutableListOf()
                detailPageDataMap[9] = GroupDetailInsightListUiModel(adGroups = adGroups.subList(0, 5))

//                detailPageDataMap[8] = GroupDetailEmptyStateUiModel(
//                    EmptyStateData.getData()
//                )
//                detailPageDataMap.remove(TYPE_CHIPS)
            }
            detailPageDataMap
        } else {
            val selectedIndex = position + 2
            for (i in TYPE_INSIGHT..TYPE_CHIPS) {
                detailPageDataMap[i]?.let { map.put(i, it) }
            }
            for (i in TYPE_POSITIVE_KEYWORD until detailPageDataMap.size) {
                if (i == selectedIndex) {
                    detailPageDataMap[i]?.let { map.put(i, it) }
                    if (detailPageDataMap[i]?.isAvailable() == false) {
                        map[8] = getEmptyStateData(position)
                    } else {
                        detailPageDataMap.remove(8)
                        map.remove(8)
                    }
                } else {
                    map[i] = GroupInsightsUiModel()
                }
            }
            map
        }
    }

    private fun getEmptyStateData(type: Int): GroupDetailEmptyStateUiModel {
        return when (type) {
            1, 2, 5 -> {
                GroupDetailEmptyStateUiModel(
                    listOf(EmptyStateData.getData()[1])
                )
            }
            3 -> {
                GroupDetailEmptyStateUiModel(
                    listOf(EmptyStateData.getData()[2], EmptyStateData.getData()[3])
                )
            }
            else -> {
                GroupDetailEmptyStateUiModel(
                    listOf(EmptyStateData.getData()[4])
                )
            }
        }
    }

    fun toAdGroupUiModelList(adGroupList: MutableList<TopAdsListAllInsightCountsResponse.TopAdsListAllInsightCounts.AdGroup>): MutableList<InsightListUiModel> {
        val list = mutableListOf<InsightListUiModel>()
        for (adGroup in adGroupList) {
            list.add(
                AdGroupUiModel(
                    adGroupName =adGroup.adGroupName,
                    count = adGroup.count,
                    adGroupID = adGroup.adGroupID,
                    adGroupType = adGroup.adGroupType
                )
            )
        }
        return list
    }
}

