package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_CHIPS
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PERFORMANCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.data.mapper.GroupDetailMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsAdGroupBidInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsGetSellerInsightDataResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsTotalAdGroupsWithInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.*
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.EmptyStateData
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class TopAdsGroupDetailUseCase @Inject constructor(
    private val topAdsGetBatchKeywordInsightUseCase: TopAdsGetBatchKeywordInsightUseCase,
    private val topAdsGroupPerformanceUseCase: TopAdsGroupPerformanceUseCase,
    private val topAdsGetAdGroupBidInsightUseCase: TopAdsGetAdGroupBidInsightUseCase,
    private val topAdsGetTotalAdGroupsWithInsightUseCase: TopAdsGetTotalAdGroupsWithInsightUseCase,
    private val topAdsGetSellerInsightDataUseCase: TopAdsGetSellerInsightDataUseCase
) {

    suspend fun executeOnBackground(
        groupDetailMapper: GroupDetailMapper,
        adGroupType: Int,
        groupId: String
    ): Map<Int, GroupDetailDataModel> {
        return coroutineScope {
            val batchKeywordAsync = async { getBatchKeywordInsight(groupId) }
            val groupPerformanceAsync = async { getGroupPerformance(groupId, adGroupType.toString()) }
            val groupBidInsightAsync = async { getGroupBidInsight(groupId) }
            val groupWithInsightAsync = async { getGroupWithInsight() }
            val sellerInsightDataAsync = async { getSellerInsight(groupId) }

            val batchKeyword = batchKeywordAsync.await()
            val groupPerformance = groupPerformanceAsync.await()
            val groupBidInsight = groupBidInsightAsync.await()
            val groupWithInsight = groupWithInsightAsync.await()
            val sellerInsightData = sellerInsightDataAsync.await()
            when (groupPerformance) {
                is TopAdsListAllInsightState.Success -> {
                    groupDetailMapper.detailPageDataMap[TYPE_PERFORMANCE] = groupPerformance.data
                }
                is TopAdsListAllInsightState.Fail -> {
                    throw groupPerformance.throwable
                }
                else -> {}
            }

            when (groupWithInsight) {
                is TopAdsListAllInsightState.Success -> {
                    if (groupWithInsight.data.topAdsGetTotalAdGroupsWithInsightByShopID.totalAdGroupsWithInsight.totalAdGroupsWithInsight == 0) {
                        (groupDetailMapper.detailPageDataMap[TYPE_CHIPS] as GroupDetailChipsUiModel).isChipsAvailable =
                            false
                        groupDetailMapper.detailPageDataMap[8] = GroupDetailEmptyStateUiModel(
                            EmptyStateData.getData()
                        )
                        return@coroutineScope groupDetailMapper.reArrangedDataMap()
                    }
                }
                is TopAdsListAllInsightState.Fail -> {
                    throw groupWithInsight.throwable
                }
                else -> {}
            }

            when (batchKeyword) {
                is TopAdsListAllInsightState.Success -> {
                    val groupData =
                        batchKeyword.data.topAdsBatchGetKeywordInsightByGroupIDV3.groups.firstOrNull()?.groupData
                    groupDetailMapper.detailPageDataMap[TYPE_POSITIVE_KEYWORD] =
                        GroupInsightsUiModel(
                            "Kata Kunci",
                            "Kunjungan pembeli menurun. Pakai kata kunci...",
                            !groupData?.newPositiveKeywordsRecom.isNullOrEmpty(),
//                            false,
                            AccordianKataKunciUiModel(
                                "Kata Kunci",
                                groupData?.newPositiveKeywordsRecom
                            )
                        )
                    groupDetailMapper.detailPageDataMap[TYPE_KEYWORD_BID] = GroupInsightsUiModel(
                        "Biaya Kata Kunci",
                        "Kunjungan pembeli menurun. Pakai kata kunci...",
                        !groupData?.existingKeywordsBidRecom.isNullOrEmpty(),
//                        false,
                        AccordianKeywordBidUiModel(
                            "Biaya Kata Kunci",
                            groupData?.existingKeywordsBidRecom
                        )
                    )
                    groupDetailMapper.detailPageDataMap[TYPE_NEGATIVE_KEYWORD_BID] =
                        GroupInsightsUiModel(
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
                is TopAdsListAllInsightState.Fail -> {
                    throw batchKeyword.throwable
                }
                else -> {}
            }
            when (groupBidInsight) {
                is TopAdsListAllInsightState.Success -> {
                    val data =
                        groupBidInsight.data.topAdsBatchGetAdGroupBidInsightByGroupID.groups.firstOrNull()?.adGroupBidInsightData
                    groupDetailMapper.detailPageDataMap[TYPE_GROUP_BID] =
                        GroupInsightsUiModel(
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
                is TopAdsListAllInsightState.Fail -> {
                    throw groupBidInsight.throwable
                }
                else -> {}
            }
            when (sellerInsightData) {
                is TopAdsListAllInsightState.Success -> {
                    val data = sellerInsightData.data.getSellerInsightData.sellerInsightData
                    groupDetailMapper.detailPageDataMap[TYPE_DAILY_BUDGET] = GroupInsightsUiModel(
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
                is TopAdsListAllInsightState.Fail -> {
//                    throw sellerInsightData.throwable
                    groupDetailMapper.detailPageDataMap[TYPE_DAILY_BUDGET] = GroupInsightsUiModel(
                        "Anggaran harian",
                        "Kunjungan embellish menurun. Pakai kata kunci...",
                        true,
//                            false,
                        AccordianDailyBudgetUiModel(
                            text = "Biaya Iklan",
                            TopAdsGetSellerInsightDataResponse.GetSellerInsightData.SellerInsightData()
                        )
                    )
                }
                else -> {}
            }
            return@coroutineScope groupDetailMapper.reArrangedDataMap()
        }
    }

    private suspend fun getSellerInsight(groupId: String): TopAdsListAllInsightState<TopAdsGetSellerInsightDataResponse> {
        return try {
            topAdsGetSellerInsightDataUseCase.invoke(groupId)
        } catch (e: Exception) {
            TopAdsListAllInsightState.Fail(e)
        }
    }

    private suspend fun getGroupWithInsight(): TopAdsListAllInsightState<TopAdsTotalAdGroupsWithInsightResponse> {
        return try {
            topAdsGetTotalAdGroupsWithInsightUseCase()
        } catch (e: Exception) {
            TopAdsListAllInsightState.Fail(e)
        }
    }

    private suspend fun getGroupBidInsight(groupId: String): TopAdsListAllInsightState<TopAdsAdGroupBidInsightResponse> {
        return try {
            topAdsGetAdGroupBidInsightUseCase(groupId)
        } catch (e: Exception) {
            TopAdsListAllInsightState.Fail(e)
        }
    }

    private suspend fun getBatchKeywordInsight(groupId: String): TopAdsListAllInsightState<TopAdsBatchGroupInsightResponse> {
        return try {
            topAdsGetBatchKeywordInsightUseCase(groupId)
        } catch (e: Exception) {
            TopAdsListAllInsightState.Fail(e)
        }
    }

    private suspend fun getGroupPerformance(
        groupId: String,
        adType: String
    ): TopAdsListAllInsightState<GroupPerformanceWidgetUiModel> {
        return try {
            topAdsGroupPerformanceUseCase(groupId, adType)
        } catch (e: Exception) {
            TopAdsListAllInsightState.Fail(e)
        }
    }
}
