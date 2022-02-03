package com.tokopedia.topads.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.data.model.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsWidgetSummaryStatisticsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TopAdsDashboardViewModel @Inject constructor (
    private val summaryStatisticsUseCase: TopAdsWidgetSummaryStatisticsUseCase
) : BaseViewModel(Dispatchers.Main) {

    private val _summaryStatisticsLiveData =
        MutableLiveData<Result<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics>>()
    val summaryStatisticsLiveData: LiveData<Result<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics>> get() = _summaryStatisticsLiveData


    fun getSummaryStatistics(startDate: String, endDate: String, adTypes: String) {
        launchCatchError(block = {
            val data =
                summaryStatisticsUseCase.getSummaryStatistics(startDate, endDate, adTypes)
            if (data?.topadsWidgetSummaryStatistics?.data == null) {
                _summaryStatisticsLiveData.postValue(Fail(Throwable()))
            } else {
                _summaryStatisticsLiveData.value =
                    Success(data.topadsWidgetSummaryStatistics.data)
            }
        }, onError = {
            _summaryStatisticsLiveData.postValue(Fail(it))
        })
    }
}