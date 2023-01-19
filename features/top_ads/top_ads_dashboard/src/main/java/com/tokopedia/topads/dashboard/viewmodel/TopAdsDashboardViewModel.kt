package com.tokopedia.topads.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.domain.usecase.GetWhiteListedUserUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.dashboard.data.model.GetPersonalisedCopyResponse
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.data.model.beranda.TopAdsLatestReading
import com.tokopedia.topads.dashboard.data.model.beranda.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.topads.dashboard.data.raw.topAdsHomepageLatestReadingJson
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsWidgetSummaryStatisticsUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopadsRecommendationStatisticsUseCase
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.domain.usecase.TopAdsGetSelectedTopUpType
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

class TopAdsDashboardViewModel @Inject constructor(
    private val summaryStatisticsUseCase: TopAdsWidgetSummaryStatisticsUseCase,
    private val recommendationStatisticsUseCase: TopadsRecommendationStatisticsUseCase,
    private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
    private val autoTopUpUSeCase: TopAdsAutoTopUpUSeCase,
    private val topAdsGetSelectedTopUpType: TopAdsGetSelectedTopUpType,
    private val whiteListedUserUseCase: GetWhiteListedUserUseCase,
) : BaseViewModel(Dispatchers.Main) {

    private val _summaryStatisticsLiveData =
        MutableLiveData<Result<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics>>()
    val summaryStatisticsLiveData: LiveData<Result<TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics>> get() = _summaryStatisticsLiveData

    private val _recommendationStatsLiveData =
        MutableLiveData<Result<RecommendationStatistics.Statistics.Data>>()
    val recommendationStatsLiveData: LiveData<Result<RecommendationStatistics.Statistics.Data>> get() = _recommendationStatsLiveData

    private val _shopDeposit = MutableLiveData<Result<DepositAmount>>()
    val shopDepositLiveData: LiveData<Result<DepositAmount>> = _shopDeposit

    private val _latestReadingLiveData =
        MutableLiveData<Result<List<TopAdsLatestReading.TopAdsLatestReadingItem.Article>>>()
    val latestReadingLiveData: LiveData<Result<List<TopAdsLatestReading.TopAdsLatestReadingItem.Article>>> get() = _latestReadingLiveData

    private val _autoTopUpStatusLiveData = MutableLiveData<Result<AutoTopUpStatus>>()
    val autoTopUpStatusLiveData:LiveData<Result<AutoTopUpStatus>> = _autoTopUpStatusLiveData

    private val _getAutoTopUpDefaultSate = MutableLiveData<Result<GetPersonalisedCopyResponse.GetPersonalisedCopy.GetPersonalisedCopyData>>()
    val getAutoTopUpDefaultSate:LiveData<Result<GetPersonalisedCopyResponse.GetPersonalisedCopy.GetPersonalisedCopyData>> = _getAutoTopUpDefaultSate

    private val _isUserWhitelisted: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val isUserWhitelisted: LiveData<Result<Boolean>> = _isUserWhitelisted

    fun fetchShopDeposit() {
        topAdsGetShopDepositUseCase.execute({
            _shopDeposit.value = Success(it.topadsDashboardDeposits.data)
        }, {
            _shopDeposit.value = Fail(it)
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

    fun fetchSummaryStatistics(startDate: String, endDate: String, adTypes: String) {
        launchCatchError(block = {
            val data =
                summaryStatisticsUseCase.getSummaryStatistics(startDate, endDate, adTypes)
            _summaryStatisticsLiveData.value =
                if (data?.topadsWidgetSummaryStatistics?.widgetSummaryStatistics == null)
                    Fail(Throwable())
                else
                    Success(data.topadsWidgetSummaryStatistics.widgetSummaryStatistics)
        }, onError = {
            _summaryStatisticsLiveData.postValue(Fail(it))
        })
    }

    fun getLatestReadings() {
        launchCatchError(block =  {
            val data =
                Gson().fromJson(topAdsHomepageLatestReadingJson, TopAdsLatestReading::class.java)
            _latestReadingLiveData.value = if (data.isNotEmpty()) {
                Success(data[0].articles)
            } else {
                Fail(Throwable())
            }
        }, onError =  {
            _latestReadingLiveData.value = Fail(it)
            Timber.d(it)
        })
    }

    fun getAutoTopUpStatus() {
        autoTopUpUSeCase.setQuery()
        autoTopUpUSeCase.setParams()
        autoTopUpUSeCase.execute({ data ->
            when {
                data.response == null -> _autoTopUpStatusLiveData.value = Fail(Exception("Gagal mengambil status"))
                data.response.errors.isEmpty() -> _autoTopUpStatusLiveData.value = Success(data.response.data)
                else -> _autoTopUpStatusLiveData.value = Fail(ResponseErrorException(data.response.errors))
            }
        }, {
            _autoTopUpStatusLiveData.value = Fail(it)
        })
    }

    fun getSelectedTopUpType(){
        topAdsGetSelectedTopUpType.execute({
            val data = it.getPersonalisedCopy.getPersonalisedCopyData
            data.isAutoTopUpSelected = data.creditPerformance == "TopUpFrequently" || data.creditPerformance == "InsufficientCredit"
            _getAutoTopUpDefaultSate.value = Success(it.getPersonalisedCopy.getPersonalisedCopyData)
        }, {
            _getAutoTopUpDefaultSate.value = Fail(it)
        })
    }

    fun getWhiteListedUser() {
        whiteListedUserUseCase.setParams()
        whiteListedUserUseCase.executeQuerySafeMode(
            onSuccess = {
                it.data.forEach { data ->
                    if (data.featureName == "variant1_auto_topup_design") _isUserWhitelisted.value =
                        Success(true)
                }
            },
            onError = {
                _isUserWhitelisted.value = Fail(it)
            }
        )
    }
}
