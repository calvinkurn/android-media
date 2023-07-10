package com.tokopedia.topads.dashboard.recommendation.data.mapper

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline_usecase.model.TopAdsManageHeadlineInput2
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.common.extension.ZERO
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_3
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_4
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_5
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTION_EDIT_PARAM
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_INSIGHT_DEFAULT_STATUS
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INVALID_INSIGHT_TYPE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_DAILY_BUDGET_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_KEYWORD_BID_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_NEGATIVE_KEYWORD_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD_NAME
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
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailInsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailEmptyStateUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKataKunciUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKeywordBidUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianNegativeKeywordUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianGroupBidUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianDailyBudgetUiModel
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

    fun reSyncDetailPageData(adGroupType: Int, clickedItem: Int = INVALID_INSIGHT_TYPE, clickedChips: Int = INVALID_INSIGHT_TYPE): MutableMap<Int, GroupDetailDataModel> {
        val position = chipsList.findPositionOfSelected { it.isSelected }
        return if (position == TYPE_INSIGHT || adGroupType == TYPE_SHOP_VALUE) {
            reshuffleInsightExpansionIfRequired(clickedItem, clickedChips)
            checkAndPutDataForUnoptimisedGroup()
            handleChipsData(adGroupType)
            detailPageDataMap.toSortedMap()
        } else {
            reSyncDataForGroupInsight(position, clickedChips)
        }
    }

    private fun reSyncDataForGroupInsight(position: Int, clickedChips: Int): SortedMap<Int, GroupDetailDataModel> {
        val map = mutableMapOf<Int, GroupDetailDataModel>()
        val selectedIndex = position + CONST_2
        for (index in TYPE_INSIGHT..TYPE_CHIPS) {
            detailPageDataMap[index]?.let { map.put(index, it) }
        }
        for (index in TYPE_POSITIVE_KEYWORD until detailPageDataMap.size) {
            if (index == selectedIndex) {
                detailPageDataMap[index]?.let {
                    expandedModel(it, clickedChips)?.let { expandedModel ->
                        map[index] = expandedModel
                        detailPageDataMap.put(
                            index,
                            expandedModel
                        )
                    }
                }
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

    private fun expandedModel(
        groupDetailDataModel: GroupDetailDataModel,
        clickedChips: Int
    ): GroupInsightsUiModel? {
        return if (clickedChips == INVALID_INSIGHT_TYPE) {
            (groupDetailDataModel as? GroupInsightsUiModel)
        } else {
            (groupDetailDataModel as? GroupInsightsUiModel)?.copy(isExpanded = true)
        }
    }

    private fun handleChipsData(adGroupType: Int) {
        if (adGroupType == TYPE_PRODUCT_VALUE) {
            if (!detailPageDataMap.contains(TYPE_CHIPS) && checkIfInsightAvailable()) {
                detailPageDataMap[TYPE_CHIPS] = GroupDetailChipsUiModel()
            }
        } else {
            detailPageDataMap.remove(TYPE_CHIPS)
        }
    }

    private fun reshuffleInsightExpansionIfRequired(
        clickedItem: Int,
        clickedChips: Int
    ) {
        if (clickedItem != INVALID_INSIGHT_TYPE) {
            detailPageDataMap.values.forEach {
                val groupInsightsUiModel = it as? GroupInsightsUiModel
                if (groupInsightsUiModel != null &&
                    clickedItem != groupInsightsUiModel.type &&
                    groupInsightsUiModel.isExpanded
                ) {
                    detailPageDataMap[groupInsightsUiModel.type] =
                        groupInsightsUiModel.copy(isExpanded = false)
                }
            }
        }
        if (clickedChips != INVALID_INSIGHT_TYPE) {
            detailPageDataMap.values.forEach {
                val groupInsightsUiModel = it as? GroupInsightsUiModel
                if (groupInsightsUiModel != null) {
                    detailPageDataMap[groupInsightsUiModel.type] =
                        groupInsightsUiModel.copy(isExpanded = false)
                }
            }
        }
    }

    private fun checkAndPutDataForUnoptimisedGroup() {
        val isAnyInsightAvailable = checkIfInsightAvailable()

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
        } else if (isAnyInsightAvailable) {
            detailPageDataMap.remove(TYPE_UN_OPTIMIZED_GROUP)
        }
    }

    private fun checkIfInsightAvailable(): Boolean {
        var isAnyInsightAvailable = false
        for (value in detailPageDataMap.values) {
            if ((value as? GroupInsightsUiModel)?.isAvailable() == true) {
                isAnyInsightAvailable = true
                break
            }
        }
        return isAnyInsightAvailable
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
        val impressionSum = getImpressionSum(TYPE_POSITIVE_KEYWORD, groupData)
        return GroupInsightsUiModel(
            TYPE_POSITIVE_KEYWORD,
            INSIGHT_TYPE_POSITIVE_KEYWORD_NAME,
            impressionSum.toString(),
            !groupData?.newPositiveKeywordsRecom.isNullOrEmpty(),
            AccordianKataKunciUiModel(
                groupData?.newPositiveKeywordsRecom,
                getInsightInputModel(!groupData?.newPositiveKeywordsRecom.isNullOrEmpty()),
                maxBid = pricingDetails.topAdsGetPricingDetails.maxBid,
                minBid = pricingDetails.topAdsGetPricingDetails.minBid
            )
        )
    }

    fun convertToAccordianKeywordBidUiModel(groupData: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData?): GroupInsightsUiModel {
        val impressionSum = getImpressionSum(TYPE_KEYWORD_BID, groupData)
        return GroupInsightsUiModel(
            TYPE_KEYWORD_BID,
            INSIGHT_TYPE_KEYWORD_BID_NAME,
            impressionSum.toString(),
            !groupData?.existingKeywordsBidRecom.isNullOrEmpty(),
            AccordianKeywordBidUiModel(
                groupData?.existingKeywordsBidRecom,
                getInsightInputModel(!groupData?.existingKeywordsBidRecom.isNullOrEmpty())
            )
        )
    }

    fun convertToAccordianNegativeKeywordUiModel(groupData: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData?): GroupInsightsUiModel {
        val impressionSum = getImpressionSum(TYPE_NEGATIVE_KEYWORD_BID, groupData)
        return GroupInsightsUiModel(
            TYPE_NEGATIVE_KEYWORD_BID,
            INSIGHT_TYPE_NEGATIVE_KEYWORD_NAME,
            impressionSum.toString(),
            !groupData?.newNegativeKeywordsRecom.isNullOrEmpty(),
            AccordianNegativeKeywordUiModel(
                groupData?.newNegativeKeywordsRecom,
                getInsightInputModel(!groupData?.newNegativeKeywordsRecom.isNullOrEmpty())
            )
        )
    }

    fun convertToAccordianGroupBidUiModel(groupBidInsight: TopAdsListAllInsightState.Success<TopAdsAdGroupBidInsightResponse>): GroupInsightsUiModel {
        val data =
            groupBidInsight.data.topAdsBatchGetAdGroupBidInsightByGroupID.groups.firstOrNull()?.adGroupBidInsightData
        return GroupInsightsUiModel(
            TYPE_GROUP_BID,
            INSIGHT_TYPE_GROUP_BID_NAME,
            data?.predictedTotalImpression.toString(),
            !data?.currentBidSettings.isNullOrEmpty() || !data?.suggestionBidSettings.isNullOrEmpty(),
            AccordianGroupBidUiModel(
                groupBidInsight.data.topAdsBatchGetAdGroupBidInsightByGroupID,
                getInsightInputModel(!data?.currentBidSettings.isNullOrEmpty() || !data?.suggestionBidSettings.isNullOrEmpty())
            )
        )
    }

    fun convertToAccordianDailyBudgetUiModel(sellerInsightData: TopAdsListAllInsightState.Success<TopAdsGetSellerInsightDataResponse>): GroupInsightsUiModel {
        val data = sellerInsightData.data.getSellerInsightData.sellerInsightData
        return GroupInsightsUiModel(
            TYPE_DAILY_BUDGET,
            INSIGHT_TYPE_DAILY_BUDGET_NAME,
            sellerInsightData.data.getSellerInsightData.sellerInsightData.dailyBudgetData.firstOrNull()?.topSlotImpression.toString(),
            data.dailyBudgetData.isNotEmpty(),
            AccordianDailyBudgetUiModel(
                data,
                getInsightInputModel(data.dailyBudgetData.isNotEmpty())
            )
        )
    }

    fun convertToTopAdsManageHeadlineInput2(
        input: TopadsManagePromoGroupProductInput?,
        shopId: String,
        groupId: String?,
        source: String,
        groupName: String?
    ): TopAdsManageHeadlineInput2 {
        val keywords = mutableListOf<TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation>()
        input?.keywordOperation?.forEachIndexed { _, keywordEditInput ->
            keywords.add(
                TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation(
                    action = keywordEditInput?.action.toString(),
                    keyword = TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation.Keyword(
                        priceBid = keywordEditInput?.keyword?.price_bid?.toLong() ?: Long.ZERO,
                        status = keywordEditInput?.keyword?.status.toString(),
                        tag = keywordEditInput?.keyword?.tag.toString(),
                        type = keywordEditInput?.keyword?.type.toString()
                    )
                )
            )
        }

        return TopAdsManageHeadlineInput2(
            source = source,
            operation = TopAdsManageHeadlineInput2.Operation(
                action = ACTION_EDIT_PARAM,
                group = TopAdsManageHeadlineInput2.Operation.Group(
                    id = groupId.toString(),
                    shopID = shopId,
                    keywordOperations = keywords,
                    status = HEADLINE_INSIGHT_DEFAULT_STATUS,
                    name = groupName.toString()
                )
            )
        )
    }

    fun putInsightCount(adGroupType: Int, totalInsight: Int) {
        insightCountMap[adGroupType] = totalInsight
    }

    private fun getImpressionSum(insightType: Int, groupData: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData?): Int {
        var impressionSum = Int.ZERO
        when (insightType) {
            TYPE_POSITIVE_KEYWORD -> {
                groupData?.newPositiveKeywordsRecom?.forEach {
                    impressionSum += it.predictedImpression.toIntOrZero()
                }
            }
            TYPE_KEYWORD_BID -> {
                groupData?.existingKeywordsBidRecom?.forEach {
                    impressionSum += it.predictedImpression.toIntOrZero()
                }
            }
            TYPE_NEGATIVE_KEYWORD_BID -> {
                groupData?.newNegativeKeywordsRecom?.forEach {
                    impressionSum += it.predictedImpression.toIntOrZero()
                }
            }
        }
        return impressionSum
    }

    private fun getInsightInputModel(isInsightAvailable: Boolean): TopadsManagePromoGroupProductInput? {
        return if (isInsightAvailable) TopadsManagePromoGroupProductInput() else null
    }
}
