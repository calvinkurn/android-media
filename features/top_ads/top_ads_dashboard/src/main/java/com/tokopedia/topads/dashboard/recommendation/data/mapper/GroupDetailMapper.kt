package com.tokopedia.topads.dashboard.recommendation.data.mapper

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_3
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_4
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_5
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INVALID_INSIGHT_TYPE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_CHIPS
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_EMPTY_STATE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_INSIGHT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PERFORMANCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_SHOP_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_UN_OPTIMIZED_GROUP
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsAdGroupBidInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsGetPricingDetailsResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsGetSellerInsightDataResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsListAllInsightCountsResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.*
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.ChipsData.chipsList
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.EmptyStateData
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.views.fragments.findPositionOfSelected
import java.util.SortedMap
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

    val insightCountMap: MutableMap<Int, Int> = mutableMapOf(
        TYPE_PRODUCT_VALUE to CONST_0,
        TYPE_SHOP_VALUE to CONST_0
    )

    fun reSyncDetailPageData(adGroupType: Int, clickedItem: Int = INVALID_INSIGHT_TYPE): MutableMap<Int, GroupDetailDataModel> {
        val position = chipsList.findPositionOfSelected { it.isSelected }
        return if (position == TYPE_INSIGHT || adGroupType == TYPE_SHOP_VALUE) {
            reshuffleInsightExpansionIfRequired(clickedItem)
            checkAndPutDataForUnoptimisedGroup()
            handleChipsData(adGroupType)
            detailPageDataMap.toSortedMap()
        } else {
            reSyncDataForGroupInsight(position)
        }
    }

    private fun reSyncDataForGroupInsight(position: Int): SortedMap<Int, GroupDetailDataModel> {
        val map = mutableMapOf<Int, GroupDetailDataModel>()
        val selectedIndex = position + CONST_2
        for (index in TYPE_INSIGHT..TYPE_CHIPS) {
            detailPageDataMap[index]?.let { map.put(index, it) }
        }
        for (index in TYPE_POSITIVE_KEYWORD until detailPageDataMap.size) {
            if (index == selectedIndex) {
                detailPageDataMap[index]?.let { map.put(index, it) }
                if (detailPageDataMap[index]?.isAvailable() == false) {
                    map[TYPE_EMPTY_STATE] = getEmptyStateData(position)
                } else {
                    detailPageDataMap.remove(TYPE_EMPTY_STATE)
                    map.remove(TYPE_EMPTY_STATE)
                }
            } else {
                map[index] = GroupInsightsUiModel()
            }
        }
        return map.toSortedMap()
    }

    private fun handleChipsData(adGroupType: Int) {
        if (adGroupType == TYPE_PRODUCT_VALUE) {
            detailPageDataMap[TYPE_CHIPS] = GroupDetailChipsUiModel()
        } else {
            detailPageDataMap.remove(TYPE_CHIPS)
        }
    }

    private fun reshuffleInsightExpansionIfRequired(
        clickedItem: Int
    ) {
        detailPageDataMap.values.forEach {
            val groupInsightsUiModel = it as? GroupInsightsUiModel
            if (clickedItem != INVALID_INSIGHT_TYPE &&
                groupInsightsUiModel != null &&
                clickedItem != groupInsightsUiModel.type &&
                groupInsightsUiModel.isExpanded
            ) {
                detailPageDataMap[groupInsightsUiModel.type] =
                    groupInsightsUiModel.copy(isExpanded = false)
            }
        }
    }

    private fun checkAndPutDataForUnoptimisedGroup() {
        var isAnyInsightAvailable = false
        for (value in detailPageDataMap.values) {
            if ((value as? GroupInsightsUiModel)?.isAvailable() == true) {
                isAnyInsightAvailable = true
                break
            }
        }
        if (!isAnyInsightAvailable && detailPageDataMap[TYPE_EMPTY_STATE] == null) {
            detailPageDataMap.remove(TYPE_CHIPS)
            val adGroups =
                (detailPageDataMap[TYPE_INSIGHT] as? InsightTypeChipsUiModel)?.adGroupList
                    ?: mutableListOf()
            if (adGroups.size > CONST_5) {
                detailPageDataMap[TYPE_UN_OPTIMIZED_GROUP] =
                    GroupDetailInsightListUiModel(adGroups = adGroups.subList(Int.ZERO, CONST_5))
            } else {
                detailPageDataMap[TYPE_UN_OPTIMIZED_GROUP] = GroupDetailInsightListUiModel(adGroups = adGroups)
            }
        }
    }

    private fun getEmptyStateData(type: Int): GroupDetailEmptyStateUiModel {
        return when (type) {
            INSIGHT_TYPE_POSITIVE_KEYWORD, INSIGHT_TYPE_KEYWORD_BID, INSIGHT_TYPE_NEGATIVE_KEYWORD -> {
                GroupDetailEmptyStateUiModel(
                    listOf(EmptyStateData.getData()[CONST_1])
                )
            }
            INSIGHT_TYPE_GROUP_BID -> {
                GroupDetailEmptyStateUiModel(
                    listOf(EmptyStateData.getData()[CONST_2], EmptyStateData.getData()[CONST_3])
                )
            }
            else -> {
                GroupDetailEmptyStateUiModel(
                    listOf(EmptyStateData.getData()[CONST_4])
                )
            }
        }
    }

    fun toAdGroupUiModelList(
        adGroupList: MutableList<TopAdsListAllInsightCountsResponse.TopAdsListAllInsightCounts.AdGroup>,
        insightType: Int
    ): MutableList<InsightListUiModel> {
        val list = mutableListOf<InsightListUiModel>()
        for (adGroup in adGroupList) {
            list.add(
                AdGroupUiModel(
                    adGroupName = adGroup.adGroupName,
                    count = adGroup.count,
                    adGroupID = adGroup.adGroupID,
                    adGroupType = adGroup.adGroupType,
                    insightType = insightType
                )
            )
        }
        return list
    }

    fun mapEmptyState() {
        (detailPageDataMap[TYPE_CHIPS] as GroupDetailChipsUiModel).isChipsAvailable =
            false
        detailPageDataMap[TYPE_EMPTY_STATE] = GroupDetailEmptyStateUiModel(
            EmptyStateData.getData()
        )
    }

    fun convertToAccordianKataKunciUiModel(
        groupData: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData?,
        pricingDetails: TopAdsGetPricingDetailsResponse
    ): GroupInsightsUiModel {
        return GroupInsightsUiModel(
            TYPE_POSITIVE_KEYWORD,
            "Kata Kunci",
            "Kunjungan pembeli menurun. Pakai kata kunci...",
            !groupData?.newPositiveKeywordsRecom.isNullOrEmpty(),
//                            false,
            AccordianKataKunciUiModel(
                "Kata Kunci",
                groupData?.newPositiveKeywordsRecom,
                pricingDetails.topAdsGetPricingDetails.maxBid,
                pricingDetails.topAdsGetPricingDetails.minBid

            )
        )
    }

    fun convertToAccordianKeywordBidUiModel(groupData: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData?): GroupInsightsUiModel {
        return GroupInsightsUiModel(
            TYPE_KEYWORD_BID,
            "Biaya Kata Kunci",
            "Kunjungan pembeli menurun. Pakai kata kunci...",
            !groupData?.existingKeywordsBidRecom.isNullOrEmpty(),
//                        false,
            AccordianKeywordBidUiModel(
                "Biaya Kata Kunci",
                groupData?.existingKeywordsBidRecom
            )
        )
    }

    fun convertToAccordianNegativeKeywordUiModel(groupData: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData?): GroupInsightsUiModel {
        return GroupInsightsUiModel(
            TYPE_NEGATIVE_KEYWORD_BID,
            "Kata Kunci Negatif",
            "Kunjungan pembeli menurun. Pakai kata kunci...",
            !groupData?.newNegativeKeywordsRecom.isNullOrEmpty(),
//                            false,
            AccordianNegativeKeywordUiModel(
                "Kata Kunci Negatif",
                groupData?.newNegativeKeywordsRecom
            )
        )
    }

    fun convertToAccordianGroupBidUiModel(groupBidInsight: TopAdsListAllInsightState.Success<TopAdsAdGroupBidInsightResponse>): GroupInsightsUiModel {
        val data =
            groupBidInsight.data.topAdsBatchGetAdGroupBidInsightByGroupID.groups.firstOrNull()?.adGroupBidInsightData
        return GroupInsightsUiModel(
            TYPE_GROUP_BID,
            "Biaya Iklan",
            "Kunjungan pembeli menurun. Pakai kata kunci...",
            !data?.currentBidSettings.isNullOrEmpty() || !data?.suggestionBidSettings.isNullOrEmpty(),
//                            false,
            AccordianGroupBidUiModel(
                text = "Biaya Iklan",
                groupBidInsight.data.topAdsBatchGetAdGroupBidInsightByGroupID
            )
        )
    }

    fun convertToAccordianDailyBudgetUiModel(sellerInsightData: TopAdsListAllInsightState.Success<TopAdsGetSellerInsightDataResponse>): GroupInsightsUiModel {
        val data = sellerInsightData.data.getSellerInsightData.sellerInsightData
        return GroupInsightsUiModel(
            TYPE_DAILY_BUDGET,
            "Anggaran harian",
            "Kunjungan embellish menurun. Pakai kata kunci...",
            data.dailyBudgetData.isNotEmpty(),
//                            false,
            AccordianDailyBudgetUiModel(
                text = "Biaya Iklan",
                data
            )
        )
    }

    fun putInsightCount(adGroupType: Int, totalInsight: Int) {
        insightCountMap[adGroupType] = totalInsight
    }
}
