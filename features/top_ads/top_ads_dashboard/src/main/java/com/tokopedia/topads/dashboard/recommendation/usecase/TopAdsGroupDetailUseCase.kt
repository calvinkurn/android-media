package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.kotlin.extensions.view.isZero
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
                    val totalInsight =
                        groupWithInsight.data.topAdsGetTotalAdGroupsWithInsightByShopID.totalAdGroupsWithInsight.totalAdGroupsWithInsight
                    if (totalInsight.isZero()) {
                        groupDetailMapper.mapEmptyState()
                        return@coroutineScope groupDetailMapper.reSyncDetailPageData(adGroupType)
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
                    val kataKuchimodel =
                        groupDetailMapper.convertToAccordianKataKunciUiModel(groupData)
                    val keywordBidUiModel =
                        groupDetailMapper.convertToAccordianKeywordBidUiModel(groupData)
                    val negativeKeywordModel =
                        groupDetailMapper.convertToAccordianNegativeKeywordUiModel(groupData)
                    groupDetailMapper.detailPageDataMap[TYPE_POSITIVE_KEYWORD] = kataKuchimodel
                    groupDetailMapper.detailPageDataMap[TYPE_KEYWORD_BID] = keywordBidUiModel
                    groupDetailMapper.detailPageDataMap[TYPE_NEGATIVE_KEYWORD_BID] = negativeKeywordModel
                }
                is TopAdsListAllInsightState.Fail -> {
                    throw batchKeyword.throwable
                }
                else -> {}
            }
            when (groupBidInsight) {
                is TopAdsListAllInsightState.Success -> {
                    val groupBidUiModel = groupDetailMapper.convertToAccordianGroupBidUiModel(groupBidInsight)
                    groupDetailMapper.detailPageDataMap[TYPE_GROUP_BID] = groupBidUiModel
                }
                is TopAdsListAllInsightState.Fail -> {
                    throw groupBidInsight.throwable
                }
                else -> {}
            }
            when (sellerInsightData) {
                is TopAdsListAllInsightState.Success -> {
                    val dailyBudgetUiModel = groupDetailMapper.convertToAccordianDailyBudgetUiModel(sellerInsightData)
                    groupDetailMapper.detailPageDataMap[TYPE_DAILY_BUDGET] = dailyBudgetUiModel
                }
                is TopAdsListAllInsightState.Fail -> {
//                    throw sellerInsightData.throwable
                    groupDetailMapper.detailPageDataMap[TYPE_DAILY_BUDGET] = GroupInsightsUiModel(
                        TYPE_DAILY_BUDGET,
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
            return@coroutineScope groupDetailMapper.reSyncDetailPageData(adGroupType)
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
