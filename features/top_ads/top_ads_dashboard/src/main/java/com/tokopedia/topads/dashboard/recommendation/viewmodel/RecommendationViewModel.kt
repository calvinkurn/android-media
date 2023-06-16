package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightGqlInputSource.SOURCE_INSIGHT_CENTER_LANDING_PAGE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsTotalAdGroupsWithInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsGetShopInfoUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.EmptyStateData
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetShopInfoUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetTotalAdGroupsWithInsightUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class RecommendationViewModel @Inject constructor(
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
        MutableLiveData<TopAdsListAllInsightState<TopAdsTotalAdGroupsWithInsightResponse>>()
    val adGroupWithInsight: LiveData<TopAdsListAllInsightState<TopAdsTotalAdGroupsWithInsightResponse>>
        get() = _adGroupWithInsight

    private fun getShopInfo() {
        launchCatchError(dispatcher.main, block = {
            _shopInfo.value = topAdsGetShopInfoUseCase(source = SOURCE_INSIGHT_CENTER_LANDING_PAGE)
        }, onError = {
                _shopInfo.value = Fail(it)
            })
    }

    private fun getAdGroupWithInsight() {
        launchCatchError(dispatcher.main, block = {
            _adGroupWithInsight.value = topAdsGetTotalAdGroupsWithInsightUseCase(
                listOf(PRODUCT_KEY, HEADLINE_KEY),
                SOURCE_INSIGHT_CENTER_LANDING_PAGE
            )
        }, onError = {
                _adGroupWithInsight.value = TopAdsListAllInsightState.Fail(it)
            })
    }

    val emptyStateData = EmptyStateData.getData()
}
