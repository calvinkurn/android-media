package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightGqlInputSource.SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_PRICING_FAIL_MAX_BID_FALLBACK_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_PRICING_FAIL_MIN_BID_FALLBACK_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PERFORMANCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.Utils
import com.tokopedia.topads.dashboard.recommendation.data.mapper.GroupDetailMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsAdGroupBidInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsGetPricingDetailsResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsGetSellerInsightDataResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsTotalAdGroupsWithInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

class TopAdsGroupDetailUseCase @Inject constructor(
    private val topAdsGetBatchKeywordInsightUseCase: TopAdsGetBatchKeywordInsightUseCase,
    private val topAdsGroupPerformanceUseCase: TopAdsGroupPerformanceUseCase,
    private val topAdsGetAdGroupBidInsightUseCase: TopAdsGetAdGroupBidInsightUseCase,
    private val topAdsGetTotalAdGroupsWithInsightUseCase: TopAdsGetTotalAdGroupsWithInsightUseCase,
    private val topAdsGetSellerInsightDataUseCase: TopAdsGetSellerInsightDataUseCase,
    private val topAdsGetPricingDetailsUseCase: TopAdsGetPricingDetailsUseCase,
    private val utils: Utils
) {

    suspend fun executeOnBackground(
        groupDetailMapper: GroupDetailMapper,
        adGroupType: Int,
        groupId: String
    ): Map<Int, GroupDetailDataModel> {
        return coroutineScope {
            val pricingDetailsAsync = async { getPricingDetails(adGroupType) }
            val batchKeywordAsync = async { getBatchKeywordInsight(groupId) }
            val groupPerformanceAsync = async { getGroupPerformance(groupId, adGroupType.toString()) }
            val groupBidInsightAsync = async { getGroupBidInsight(groupId) }
            val groupWithInsightAsync = async { getGroupWithInsight(utils.convertAdTypeToString(adGroupType)) }
            val sellerInsightDataAsync = async { getSellerInsight(groupId) }

            val pricingDetails = pricingDetailsAsync.await()
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
                    groupDetailMapper.putInsightCount(adGroupType, totalInsight)
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
                        groupDetailMapper.convertToAccordianKataKunciUiModel(
                            groupData,
                            pricingDetails
                        )
                    val keywordBidUiModel =
                        groupDetailMapper.convertToAccordianKeywordBidUiModel(groupData)
                    val negativeKeywordModel =
                        groupDetailMapper.convertToAccordianNegativeKeywordUiModel(groupData)
                    groupDetailMapper.detailPageDataMap[TYPE_POSITIVE_KEYWORD] = kataKuchimodel
                    groupDetailMapper.detailPageDataMap[TYPE_KEYWORD_BID] = keywordBidUiModel
                    groupDetailMapper.detailPageDataMap[TYPE_NEGATIVE_KEYWORD_BID] = negativeKeywordModel
                }
                else -> {}
            }
            when (groupBidInsight) {
                is TopAdsListAllInsightState.Success -> {
                    val groupBidUiModel = groupDetailMapper.convertToAccordianGroupBidUiModel(groupBidInsight)
                    groupDetailMapper.detailPageDataMap[TYPE_GROUP_BID] = groupBidUiModel
                }
                else -> {}
            }
            when (sellerInsightData) {
                is TopAdsListAllInsightState.Success -> {
                    val dailyBudgetUiModel = groupDetailMapper.convertToAccordianDailyBudgetUiModel(sellerInsightData)
                    groupDetailMapper.detailPageDataMap[TYPE_DAILY_BUDGET] = dailyBudgetUiModel
                }
                else -> {}
            }
            return@coroutineScope groupDetailMapper.reSyncDetailPageData(adGroupType)
        }
    }

    private suspend fun getPricingDetails(adGroupType: Int): TopAdsGetPricingDetailsResponse {
        return try {
            topAdsGetPricingDetailsUseCase.invoke(utils.convertAdTypeToString(adGroupType))
        } catch (e: Exception) {
            Timber.d(e)
            TopAdsGetPricingDetailsResponse(
                TopAdsGetPricingDetailsResponse.TopAdsGetPricingDetails(
                    maxBid = INSIGHT_PRICING_FAIL_MAX_BID_FALLBACK_VALUE,
                    minBid = INSIGHT_PRICING_FAIL_MIN_BID_FALLBACK_VALUE
                )
            )
        }
    }

    private suspend fun getSellerInsight(groupId: String): TopAdsListAllInsightState<TopAdsGetSellerInsightDataResponse> {
        return try {
            topAdsGetSellerInsightDataUseCase.invoke(groupId)
        } catch (e: Exception) {
            TopAdsListAllInsightState.Fail(e)
        }
    }

    private suspend fun getGroupWithInsight(AdGroupType: String): TopAdsListAllInsightState<TopAdsTotalAdGroupsWithInsightResponse> {
        return try {
            topAdsGetTotalAdGroupsWithInsightUseCase(listOf(AdGroupType), SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE)
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
