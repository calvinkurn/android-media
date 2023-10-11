package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.model.TopadsInsightProductsResponse
import com.tokopedia.topads.common.domain.usecase.TopAdsInsightProductsUseCase
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.domain.interactor.TopadsRecommendationStatisticsUseCase
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightGqlInputSource.SOURCE_INSIGHT_CENTER_LANDING_PAGE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightCountUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsGetShopInfoUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.EmptyStateData
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetShopInfoUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetTotalAdGroupsWithInsightUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class RecommendationViewModel @Inject constructor(
    private val recommendationStatisticsUseCase: TopadsRecommendationStatisticsUseCase,
    private val topAdsInsightProductsUseCase: TopAdsInsightProductsUseCase,
    private val dispatcher: CoroutineDispatchers,
    private val topAdsGetShopInfoUseCase: TopAdsGetShopInfoUseCase,
    private val topAdsGetTotalAdGroupsWithInsightUseCase: TopAdsGetTotalAdGroupsWithInsightUseCase
) : BaseViewModel(dispatcher.main) {

    fun loadRecommendationPage() {
        getShopInfo()
        getAdGroupWithInsight()
    }

    private val _shopInfo =
        MutableLiveData<Result<TopAdsGetShopInfoUiModel>>()
    val shopInfo: LiveData<Result<TopAdsGetShopInfoUiModel>>
        get() = _shopInfo

    private val _adGroupWithInsight =
        MutableLiveData<TopAdsListAllInsightState<GroupInsightCountUiModel>>()
    val adGroupWithInsight: LiveData<TopAdsListAllInsightState<GroupInsightCountUiModel>>
        get() = _adGroupWithInsight

    private val _recommendationStatsLiveData = MutableLiveData<Result<RecommendationStatistics.Statistics.Data>>()
    val recommendationStatsLiveData: LiveData<Result<RecommendationStatistics.Statistics.Data>>
        get() = _recommendationStatsLiveData

    private val _productInsightLiveData = MutableLiveData<Result<TopadsInsightProductsResponse>>()
    val productInsightLiveData: LiveData<Result<TopadsInsightProductsResponse>>
        get() = _productInsightLiveData

    private fun getShopInfo() {
        launchCatchError(dispatcher.main, block = {
            _shopInfo.value = topAdsGetShopInfoUseCase(source = SOURCE_INSIGHT_CENTER_LANDING_PAGE)
        }, onError = {
                _shopInfo.value = Fail(it)
            })
    }

    fun fetchRecommendationStatistics() {
        launchCatchError(block = {
            val data = recommendationStatisticsUseCase.fetchRecommendationStatistics()
            _recommendationStatsLiveData.value =
                if (data?.statistics?.data != null) {
                    Success(data.statistics.data)
                } else {
                    Fail(Throwable())
                }
        }, onError = {
                _recommendationStatsLiveData.value = Fail(it)
            })
    }

    fun getOutOfStockProducts() {
        launchCatchError(block = {
            val data = topAdsInsightProductsUseCase(String.EMPTY)
            _productInsightLiveData.value =
                if (data.topadsInsightProducts?.errors.isNullOrEmpty())
                    Success(data)
                else
                    Fail(Throwable())
        }, onError = {
            _productInsightLiveData.value = Fail(it)
        })
    }

    private fun getAdGroupWithInsight() {
        launchCatchError(dispatcher.main, block = {
            val productInsightCount = topAdsGetTotalAdGroupsWithInsightUseCase(
                listOf(PRODUCT_KEY),
                SOURCE_INSIGHT_CENTER_LANDING_PAGE
            )
            val headlineInsightCount = topAdsGetTotalAdGroupsWithInsightUseCase(
                listOf(HEADLINE_KEY),
                SOURCE_INSIGHT_CENTER_LANDING_PAGE
            )

            val count = GroupInsightCountUiModel(
                if (productInsightCount is TopAdsListAllInsightState.Success) {
                    productInsightCount.data.topAdsGetTotalAdGroupsWithInsightByShopID.totalAdGroupsWithInsight.totalAdGroupsWithInsight
                } else {
                    Int.ZERO
                },
                if (headlineInsightCount is TopAdsListAllInsightState.Success) {
                    headlineInsightCount.data.topAdsGetTotalAdGroupsWithInsightByShopID.totalAdGroupsWithInsight.totalAdGroupsWithInsight
                } else {
                    Int.ZERO
                }
            )

            _adGroupWithInsight.value = TopAdsListAllInsightState.Success(count)
        }, onError = {
                _adGroupWithInsight.value = TopAdsListAllInsightState.Fail(it)
            })
    }

    val emptyStateData = EmptyStateData.getData()
}
