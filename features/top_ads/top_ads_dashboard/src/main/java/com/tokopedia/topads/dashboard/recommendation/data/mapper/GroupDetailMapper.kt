package com.tokopedia.topads.dashboard.recommendation.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline_usecase.model.TopAdsManageHeadlineInput2
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTION_EDIT_PARAM
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INVALID_INSIGHT_TYPE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_DAILY_BUDGET_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_KEYWORD_BID_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_NEGATIVE_KEYWORD_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEYWORD_TYPE_POSITIVE_EXACT
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
        val map = mutableMapOf<Int, GroupDetailDataModel>()
        val position = chipsList.findPositionOfSelected { it.isSelected }
        return if (position == TYPE_INSIGHT || adGroupType == TYPE_SHOP_VALUE) {
            detailPageDataMap
            var isPresent = false
            detailPageDataMap.values.forEach {
                val groupInsightsUiModel = it as? GroupInsightsUiModel
                if (groupInsightsUiModel?.isAvailable() == true) isPresent = true
                reshuffleInsightExpansion(clickedItem, groupInsightsUiModel)
            }
            if (!isPresent && detailPageDataMap[TYPE_EMPTY_STATE] == null) {
                addDataForUnoptimisedGroup()
            }
            handleChipsData(adGroupType)
            detailPageDataMap.toSortedMap()
        } else {
            val selectedIndex = position + 2
            for (i in TYPE_INSIGHT..TYPE_CHIPS) {
                detailPageDataMap[i]?.let { map.put(i, it) }
            }
            for (i in TYPE_POSITIVE_KEYWORD until detailPageDataMap.size) {
                if (i == selectedIndex) {
                    detailPageDataMap[i]?.let { map.put(i, it) }
                    if (detailPageDataMap[i]?.isAvailable() == false) {
                        map[TYPE_EMPTY_STATE] = getEmptyStateData(position)
                    } else {
                        detailPageDataMap.remove(TYPE_EMPTY_STATE)
                        map.remove(TYPE_EMPTY_STATE)
                    }
                } else {
                    map[i] = GroupInsightsUiModel()
                }
            }
            map.toSortedMap()
        }
    }

    private fun handleChipsData(adGroupType: Int) {
        if (adGroupType == TYPE_PRODUCT_VALUE) {
            detailPageDataMap[TYPE_CHIPS] = GroupDetailChipsUiModel()
        } else {
            detailPageDataMap.remove(TYPE_CHIPS)
        }
    }

    private fun reshuffleInsightExpansion(
        clickedItem: Int,
        groupInsightsUiModel: GroupInsightsUiModel?
    ) {
        if (clickedItem != INVALID_INSIGHT_TYPE && groupInsightsUiModel != null) {
            if (clickedItem != groupInsightsUiModel.type && groupInsightsUiModel.isExpanded) {
                detailPageDataMap[groupInsightsUiModel.type] =
                    groupInsightsUiModel.copy(isExpanded = false)
            }
        }
    }

    private fun addDataForUnoptimisedGroup() {
        detailPageDataMap.remove(TYPE_CHIPS)
        val adGroups =
            (detailPageDataMap[TYPE_INSIGHT] as? InsightTypeChipsUiModel)?.adGroupList
                ?: mutableListOf()
        if (adGroups.size > 5) {
            detailPageDataMap[TYPE_UN_OPTIMIZED_GROUP] =
                GroupDetailInsightListUiModel(adGroups = adGroups.subList(0, 5))
        } else {
            detailPageDataMap[TYPE_UN_OPTIMIZED_GROUP] = GroupDetailInsightListUiModel(adGroups = adGroups)
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
        var impressionSum = 0
        groupData?.newPositiveKeywordsRecom?.forEach {
            impressionSum += it.predictedImpression.toIntOrZero()
        }
        return GroupInsightsUiModel(
            TYPE_POSITIVE_KEYWORD,
            INSIGHT_TYPE_POSITIVE_KEYWORD_NAME,
            "Kunjungan pembeli menurun. Pakai kata kunci populer untuk potensi tampil +$impressionSum kali/hari.",
            !groupData?.newPositiveKeywordsRecom.isNullOrEmpty(),
//                            false,
            AccordianKataKunciUiModel(
                "Kata Kunci",
                groupData?.newPositiveKeywordsRecom,
                maxBid = pricingDetails.topAdsGetPricingDetails.maxBid,
                minBid = pricingDetails.topAdsGetPricingDetails.minBid
            )
        )
    }

    fun convertToAccordianKeywordBidUiModel(groupData: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData?): GroupInsightsUiModel {
        var impressionSum = 0
        groupData?.existingKeywordsBidRecom?.forEach {
            impressionSum += it.predictedImpression.toIntOrZero()
        }

        return GroupInsightsUiModel(
            TYPE_KEYWORD_BID,
            INSIGHT_TYPE_KEYWORD_BID_NAME,
            "Kata kuncimu kalah saing. Sesuaikan biaya kata kunci untuk potensi tampil +$impressionSum kali/hari.",
            !groupData?.existingKeywordsBidRecom.isNullOrEmpty(),
//                        false,
            AccordianKeywordBidUiModel(
                "Biaya Kata Kunci",
                groupData?.existingKeywordsBidRecom
            )
        )
    }

    fun convertToAccordianNegativeKeywordUiModel(groupData: TopAdsBatchGroupInsightResponse.TopAdsBatchGetKeywordInsightByGroupIDV3.Group.GroupData?): GroupInsightsUiModel {
        var impressionSum = 0
        groupData?.newNegativeKeywordsRecom?.forEach {
            impressionSum += it.predictedImpression.toIntOrZero()
        }

        return GroupInsightsUiModel(
            TYPE_NEGATIVE_KEYWORD_BID,
            INSIGHT_TYPE_NEGATIVE_KEYWORD_NAME,
            "Iklanmu kurang efektif & boros. Potensi hemat Rp$impressionSum/hari dengan kata kunci negatif.",
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
            INSIGHT_TYPE_GROUP_BID_NAME,
            "Total tampil menurun, nih. Tambah biaya iklan untuk potensi tampil +${data?.predictedTotalImpression} kali/hari.",
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
            INSIGHT_TYPE_DAILY_BUDGET_NAME,
            "Durasi iklan belum maksimal. Tambah anggaran untuk potensi klik +${sellerInsightData.data.getSellerInsightData.sellerInsightData.dailyBudgetData.firstOrNull()?.topSlotImpression} klik/hari.",
            data.dailyBudgetData.isNotEmpty(),
//                            false,
            AccordianDailyBudgetUiModel(
                text = "Biaya Iklan",
                data
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
        input?.keywordOperation?.forEachIndexed { index, keywordEditInput ->
            keywords.add(
                TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation(
                    action = keywordEditInput?.action.toString(),
                    keyword = TopAdsManageHeadlineInput2.Operation.Group.KeywordOperation.Keyword(
                        priceBid = keywordEditInput?.keyword?.price_bid?.toLong() ?: 0,
//                        status = keywordEditInput?.keyword?.status.toString(),
                        status = ACTIVE_KEYWORD,
                        tag = keywordEditInput?.keyword?.tag.toString(),
//                        type = keywordEditInput?.keyword?.type.toString()
                        type = KEYWORD_TYPE_POSITIVE_EXACT,
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
                    status = "published",
                    name = groupName.toString()
                )
            )
        )
    }

    fun putInsightCount(adGroupType: Int, totalInsight: Int) {
        insightCountMap[adGroupType] = totalInsight
    }
}
