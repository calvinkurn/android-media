package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_1
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_3
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_4
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightGqlInputSource.SOURCE_INSIGHT_CENTER_LANDING_PAGE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_ALL_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_DAILY_BUDGET_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_KEYWORD_BID_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_NEGATIVE_KEYWORD_NAME
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD_NAME
import com.tokopedia.topads.dashboard.recommendation.data.mapper.InsightDataMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyStateUiListModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.SaranTopAdsChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.EmptyStateData
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsListAllInsightCountsUseCase
import javax.inject.Inject

class TopAdsListAllInsightViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val topAdsListAllInsightCountsUseCase: TopAdsListAllInsightCountsUseCase
) : BaseViewModel(dispatcher.main) {

    private val _productInsights =
        MutableLiveData<TopAdsListAllInsightState<InsightUiModel>>()
    val productInsights: LiveData<TopAdsListAllInsightState<InsightUiModel>>
        get() = _productInsights

    private val _headlineInsights =
        MutableLiveData<TopAdsListAllInsightState<InsightUiModel>>()
    val headlineInsights: LiveData<TopAdsListAllInsightState<InsightUiModel>>
        get() = _headlineInsights

    fun getFirstPageData(
        adGroupType: String,
        insightType: Int,
        mapper: InsightDataMapper?
    ) {
        launchCatchError(dispatcher.main, block = {
            if (adGroupType == RecommendationConstants.PRODUCT_KEY) {
                _productInsights.value = TopAdsListAllInsightState.Loading(insightType)
                val data = topAdsListAllInsightCountsUseCase.invoke(
                    source = SOURCE_INSIGHT_CENTER_LANDING_PAGE,
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = String.EMPTY,
                    mapper = mapper
                )
                val state = TopAdsListAllInsightState.Success(data.toInsightUiModel(insightType))
                _productInsights.value = state
            } else {
                val data = topAdsListAllInsightCountsUseCase(
                    source = SOURCE_INSIGHT_CENTER_LANDING_PAGE,
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = String.EMPTY,
                    mapper = mapper
                )
                val state = TopAdsListAllInsightState.Success(data.toInsightUiModel(insightType))
                _headlineInsights.value = state
            }
        }, onError = {
                _productInsights.value = TopAdsListAllInsightState.Fail(it)
            })
    }

    fun getNextPageData(
        adGroupType: String,
        insightType: Int,
        startCursor: String,
        mapper: InsightDataMapper
    ) {
        launchCatchError(dispatcher.main, block = {
            if (adGroupType == RecommendationConstants.PRODUCT_KEY) {
                _productInsights.value = TopAdsListAllInsightState.Loading(insightType)
                val data = topAdsListAllInsightCountsUseCase(
                    source = SOURCE_INSIGHT_CENTER_LANDING_PAGE,
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = startCursor,
                    mapper = mapper
                )
                val state = TopAdsListAllInsightState.Success(data.toInsightUiModel(insightType))
                _productInsights.value = state
            } else {
                val data = topAdsListAllInsightCountsUseCase(
                    source = SOURCE_INSIGHT_CENTER_LANDING_PAGE,
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = startCursor,
                    mapper = mapper
                )
                val state = TopAdsListAllInsightState.Success(data.toInsightUiModel(insightType))
                _headlineInsights.value = state
            }
        }, onError = {
                _productInsights.value = TopAdsListAllInsightState.Fail(it)
            })
    }

    fun getChipsData(): MutableList<SaranTopAdsChipsUiModel> {
        return mutableListOf(
            SaranTopAdsChipsUiModel(INSIGHT_TYPE_ALL_NAME, true),
            SaranTopAdsChipsUiModel(INSIGHT_TYPE_POSITIVE_KEYWORD_NAME),
            SaranTopAdsChipsUiModel(INSIGHT_TYPE_KEYWORD_BID_NAME),
            SaranTopAdsChipsUiModel(INSIGHT_TYPE_GROUP_BID_NAME),
            SaranTopAdsChipsUiModel(INSIGHT_TYPE_DAILY_BUDGET_NAME),
            SaranTopAdsChipsUiModel(INSIGHT_TYPE_NEGATIVE_KEYWORD_NAME)
        )
    }

    fun getEmptyStateData(type: Int): InsightListUiModel {
        return when (type) {
            INSIGHT_TYPE_POSITIVE_KEYWORD, INSIGHT_TYPE_KEYWORD_BID, INSIGHT_TYPE_NEGATIVE_KEYWORD -> {
                EmptyStateUiListModel(
                    getChipsData()[type].name,
                    listOf(EmptyStateData.getData()[CONST_1])
                )
            }
            INSIGHT_TYPE_GROUP_BID -> {
                EmptyStateUiListModel(
                    getChipsData()[type].name,
                    listOf(EmptyStateData.getData()[CONST_2], EmptyStateData.getData()[CONST_3])
                )
            }
            else -> {
                EmptyStateUiListModel(
                    getChipsData()[type].name,
                    listOf(EmptyStateData.getData()[CONST_4])
                )
            }
        }
    }
}
