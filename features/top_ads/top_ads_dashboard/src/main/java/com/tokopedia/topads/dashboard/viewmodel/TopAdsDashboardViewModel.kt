package com.tokopedia.topads.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.data.model.beranda.TopAdsLatestReading
import com.tokopedia.topads.dashboard.data.model.beranda.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.topads.dashboard.data.raw.topAdsHomepageLatestReadingJson
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsWidgetSummaryStatisticsUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopadsRecommendationStatisticsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TopAdsDashboardViewModel @Inject constructor(
    private val summaryStatisticsUseCase: TopAdsWidgetSummaryStatisticsUseCase,
    private val recommendationStatisticsUseCase: TopadsRecommendationStatisticsUseCase
) : BaseViewModel(Dispatchers.Main) {

    private val _summaryStatisticsLiveData =
        MutableLiveData<Result<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics>>()
    val summaryStatisticsLiveData: LiveData<Result<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics>> get() = _summaryStatisticsLiveData

    private val _recommendationStatsLiveData =
        MutableLiveData<Result<RecommendationStatistics.Statistics.Data>>()
    val recommendationStatsLiveData: LiveData<Result<RecommendationStatistics.Statistics.Data>> get() = _recommendationStatsLiveData


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
            _recommendationStatsLiveData.value = Fail(Throwable())
        })
    }

    fun fetchSummaryStatistics(startDate: String, endDate: String, adTypes: String) {
        launchCatchError(block = {
            val data =
                summaryStatisticsUseCase.getSummaryStatistics(startDate, endDate, adTypes)
            _summaryStatisticsLiveData.value =
                if (data?.topadsWidgetSummaryStatistics?.data == null)
                    Fail(Throwable())
                else
                    Success(data.topadsWidgetSummaryStatistics.data)
        }, onError = {
            _summaryStatisticsLiveData.postValue(Fail(it))
        })
    }

    fun getLatestReadings(): List<TopAdsLatestReading.TopAdsLatestReadingItem.Article> {
        val data = Gson().fromJson(topAdsHomepageLatestReadingJson,TopAdsLatestReading::class.java)
        return data[0].articles
    }
}