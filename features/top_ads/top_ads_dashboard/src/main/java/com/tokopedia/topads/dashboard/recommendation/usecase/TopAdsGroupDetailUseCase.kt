package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PERFORMANCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.data.mapper.GroupDetailMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsAdGroupBidInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class TopAdsGroupDetailUseCase @Inject constructor(
    private val topAdsGetBatchKeywordInsightUseCase: TopAdsGetBatchKeywordInsightUseCase,
    private val topAdsGroupPerformanceUseCase: TopAdsGroupPerformanceUseCase,
    private val topAdsGetAdGroupBidInsightUseCase: TopAdsGetAdGroupBidInsightUseCase
) {

    suspend fun executeOnBackground(
        groupDetailMapper: GroupDetailMapper,
        adGroupType: Int,
        groupId: String
    ): Map<Int, GroupDetailDataModel> {
        return coroutineScope {
            val batchKeywordAsync = async { getBatchKeywordInsight(groupId) }
            val groupPerformanceAsync =
                async { getGroupPerformance(groupId, adGroupType.toString()) }
            val groupBidInsightAsync = async { getGroupBidInsight(groupId) }

            val batchKeyword = batchKeywordAsync.await()
            val groupPerformance = groupPerformanceAsync.await()
            val groupBidInsight = groupBidInsightAsync.await()
            when (groupPerformance) {
                is TopAdsListAllInsightState.Success -> {
                    groupDetailMapper.detailPageDataMap[TYPE_PERFORMANCE] = groupPerformance.data
                }
                is TopAdsListAllInsightState.Fail -> {
                    throw groupPerformance.throwable
                }
                else -> {}
            }
//            (groupDetailMapper.detailPageDataMap[TYPE_CHIPS] as GroupDetailChipsUiModel).isChipsAvailable = false
//            groupDetailMapper.detailPageDataMap[8] = GroupDetailEmptyStateUiModel(
//                EmptyStateData.getData()
//            )
//            if(true)return@coroutineScope groupDetailMapper.reArrangedDataMap()
//            if ((groupDetailMapper.detailPageDataMap[TYPE_PERFORMANCE] as GroupPerformanceWidgetUiModel).)
            when (batchKeyword) {
                is TopAdsListAllInsightState.Success -> {
                    val groupData =
                        batchKeyword.data.topAdsBatchGetKeywordInsightByGroupIDV3.groups.firstOrNull()?.groupData
                    groupDetailMapper.detailPageDataMap[TYPE_POSITIVE_KEYWORD] =
                        GroupInsightsUiModel(
                            "Kata Kunci",
                            "Kunjungan pembeli menurun. Pakai kata kunci...",
//                            !groupData?.existingKeywordsBidRecom.isNullOrEmpty(),
                            false,
                            AccordianKataKunciUiModel(
                                "Kata Kunci",
                                groupData?.existingKeywordsBidRecom
                            )
                        )
                    groupDetailMapper.detailPageDataMap[TYPE_KEYWORD_BID] = GroupInsightsUiModel(
                        "Biaya Kata Kunci",
                        "Kunjungan pembeli menurun. Pakai kata kunci...",
//                        !groupData?.newPositiveKeywordsRecom.isNullOrEmpty(),
                        false,
                        AccordianKeywordBidUiModel(
                            "Biaya Kata Kunci",
                            groupData?.newPositiveKeywordsRecom
                        )
                    )
                    groupDetailMapper.detailPageDataMap[TYPE_NEGATIVE_KEYWORD_BID] =
                        GroupInsightsUiModel(
                            "Kata Kunci Negatif",
                            "Kunjungan pembeli menurun. Pakai kata kunci...",
//                            !groupData?.newNegativeKeywordsRecom.isNullOrEmpty(),
                            false,
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
//                            !data?.currentBidSettings.isNullOrEmpty() || !data?.suggestionBidSettings.isNullOrEmpty(),
                            false,
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
            return@coroutineScope groupDetailMapper.reArrangedDataMap()
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
