package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsListAllInsightCountsUseCase
import javax.inject.Inject

class TopAdsListAllInsightViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val topAdsListAllInsightCountsUseCase: TopAdsListAllInsightCountsUseCase,
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
    ) {
        launchCatchError(dispatcher.main, block = {
            if (adGroupType == "product") {
                _productInsights.value = TopAdsListAllInsightState.Loading(insightType)
                _productInsights.value = topAdsListAllInsightCountsUseCase(
                    source = "insightcenter.test",
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = ""
                )
            } else {
                _headlineInsights.value = topAdsListAllInsightCountsUseCase(
                    source = "insightcenter.test",
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = ""
                )
            }

        }, onError = {
            _productInsights.value = TopAdsListAllInsightState.Fail(it)
        })
    }

    fun getNextPageData(
        adGroupType: String,
        insightType: Int,
        startCursor: String,
    ) {
        launchCatchError(dispatcher.main, block = {
            if (adGroupType == "product") {
                _productInsights.value = TopAdsListAllInsightState.Loading(insightType)
                val data  = topAdsListAllInsightCountsUseCase(
                    source = "insightcenter.test",
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = startCursor
                )
                _productInsights.value = data
            } else {
                _headlineInsights.value = topAdsListAllInsightCountsUseCase(
                    source = "insightcenter.test",
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = startCursor
                )
            }

        }, onError = {
            _productInsights.value = TopAdsListAllInsightState.Fail(it)
        })
    }

}
