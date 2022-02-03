package com.tokopedia.topads.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.data.model.beranda.TopAdsLatestReading
import com.tokopedia.topads.dashboard.data.model.beranda.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsLatestReadingUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsWidgetSummaryStatisticsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TopAdsDashboardViewModel @Inject constructor(
    private val summaryStatisticsUseCase: TopAdsWidgetSummaryStatisticsUseCase,
    private val topAdsLatestReadingUseCase: TopAdsLatestReadingUseCase
) : BaseViewModel(Dispatchers.Main) {

    private val _summaryStatisticsLiveData =
        MutableLiveData<Result<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics.Summary>>()
    val summaryStatisticsLiveData: LiveData<Result<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics.Summary>> get() = _summaryStatisticsLiveData

    private val _latestReadingLiveData =
        MutableLiveData<Result<List<TopAdsLatestReading.CategoryTree.Data.Category>>>()
    val latestReadingLiveData: LiveData<Result<List<TopAdsLatestReading.CategoryTree.Data.Category>>> get() = _latestReadingLiveData


    fun fetchLatestReading() {
        launchCatchError(block = {
            val data = topAdsLatestReadingUseCase.getLatestReading()
            _latestReadingLiveData.value =
                if (data?.categoryTree?.data == null)
                    Fail(Throwable())
                else
                    Success(data.categoryTree.data.categories)
        }, onError = {
            _latestReadingLiveData.value = Fail(it)
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
                    Success(data.topadsWidgetSummaryStatistics.data.summary)
        }, onError = {
            _summaryStatisticsLiveData.postValue(Fail(it))
        })
    }
}