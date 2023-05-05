package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.recommendation.data.mapper.InsightDataMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.SaranTopAdsChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyStateUiListModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.EmptyStateData
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
        mapper: InsightDataMapper,
    ) {
        launchCatchError(dispatcher.main, block = {
            if (adGroupType == "product") {
                _productInsights.value = TopAdsListAllInsightState.Loading(insightType)
                _productInsights.value = topAdsListAllInsightCountsUseCase.invoke(
                    source = "gql.list_all_insight_counts.test",
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = "",
                    mapper = mapper
                )
            } else {
                _headlineInsights.value = topAdsListAllInsightCountsUseCase(
                    source = "gql.list_all_insight_counts.test",
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = "",
                    mapper = mapper
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
        mapper: InsightDataMapper
    ) {
        launchCatchError(dispatcher.main, block = {
            if (adGroupType == "product") {
                _productInsights.value = TopAdsListAllInsightState.Loading(insightType)
                val data  = topAdsListAllInsightCountsUseCase(
                    source = "gql.list_all_insight_counts.test",
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = startCursor,
                    mapper = mapper
                )
                _productInsights.value = data
            } else {
                _headlineInsights.value = topAdsListAllInsightCountsUseCase(
                    source = "gql.list_all_insight_counts.test",
                    adGroupType = adGroupType,
                    insightType = insightType,
                    startCursor = startCursor,
                    mapper = mapper
                )
            }

        }, onError = {
            _productInsights.value = TopAdsListAllInsightState.Fail(it)
        })
    }

    fun getChipsData(): MutableList<SaranTopAdsChipsUiModel> {
        return mutableListOf(
            SaranTopAdsChipsUiModel("Semua", true),
            SaranTopAdsChipsUiModel("Kata Kunci"),
            SaranTopAdsChipsUiModel("Biaya Kata Kunci"),
            SaranTopAdsChipsUiModel("Biaya Iklan"),
            SaranTopAdsChipsUiModel("Anggaran Harian"),
            SaranTopAdsChipsUiModel("Kata Kunci Negatif"),
        )
    }

    fun getEmptyStateData(type: Int): InsightListUiModel {
        return when (type) {
            1, 2, 5 -> {
                EmptyStateUiListModel(
                    getChipsData()[type].name,
                    listOf(EmptyStateData.getData()[1])
                )
            }
            3 -> {
                EmptyStateUiListModel(
                    getChipsData()[type].name,
                    listOf(EmptyStateData.getData()[2], EmptyStateData.getData()[3])
                )
            }
            else -> {
                EmptyStateUiListModel(
                    getChipsData()[type].name,
                    listOf(EmptyStateData.getData()[4])
                )
            }
        }
    }

}
