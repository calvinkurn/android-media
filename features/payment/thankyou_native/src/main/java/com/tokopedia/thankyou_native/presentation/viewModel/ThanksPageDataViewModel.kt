package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressResponse
import com.tokopedia.thankyou_native.data.mapper.FeatureRecommendationMapper
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.model.WalletBalance
import com.tokopedia.thankyou_native.domain.usecase.*
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ThanksPageDataViewModel @Inject constructor(
    private val thanksPageDataUseCase: ThanksPageDataUseCase,
    private val thanksPageMapperUseCase: ThanksPageMapperUseCase,
    private val gyroEngineRequestUseCase: GyroEngineRequestUseCase,
    private val fetchWalletBalanceUseCase: FetchWalletBalanceUseCase,
    private val gyroEngineMapperUseCase: GyroEngineMapperUseCase,
    private val topTickerDataUseCase: TopTickerUseCase,
    private val getDefaultAddressUseCase: GetDefaultAddressUseCase,
    private val thankYouTopAdsViewModelUseCase: ThankYouTopAdsViewModelUseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _thanksPageDataResultLiveData = MutableLiveData<Result<ThanksPageData>>()
    val thanksPageDataResultLiveData: LiveData<Result<ThanksPageData>> =
        _thanksPageDataResultLiveData

    private val _gyroRecommendationLiveData = MutableLiveData<GyroRecommendation>()
    val gyroRecommendationLiveData: LiveData<GyroRecommendation> = _gyroRecommendationLiveData

    private val _topTickerLiveData = MutableLiveData<Result<List<TickerData>>>()
    val topTickerLiveData: LiveData<Result<List<TickerData>>> = _topTickerLiveData

    private val _defaultAddressLiveData = MutableLiveData<Result<GetDefaultChosenAddressResponse>>()
    val defaultAddressLiveData: LiveData<Result<GetDefaultChosenAddressResponse>> =
        _defaultAddressLiveData

    private val _topAdsDataLiveData = MutableLiveData<TopAdsRequestParams>()
    val topAdsDataLiveData: LiveData<TopAdsRequestParams> = _topAdsDataLiveData

    private val _gyroResponseLiveData = MutableLiveData<FeatureEngineData>()
    val gyroResponseLiveData: LiveData<FeatureEngineData> = _gyroResponseLiveData


    fun getThanksPageData(paymentId: String, merchant: String) {
        thanksPageDataUseCase.cancelJobs()
        thanksPageDataUseCase.getThankPageData(
            ::onThanksPageDataSuccess,
            ::onThanksPageDataError,
            paymentId,
            merchant
        )
    }

    fun checkForGoPayActivation(thanksPageData: ThanksPageData) {
        fetchWalletBalanceUseCase.cancelJobs()
        fetchWalletBalanceUseCase.getGoPayBalance {
            getFeatureEngine(thanksPageData, it)
        }
    }

    @VisibleForTesting
    fun getFeatureEngine(thanksPageData: ThanksPageData, walletBalance: WalletBalance?) {
        gyroEngineRequestUseCase.cancelJobs()
        gyroEngineRequestUseCase.getFeatureEngineData(
            thanksPageData,
            walletBalance
        ) {
            if (it.success) {
                it.engineData?.let { featureEngineData ->
                    _gyroResponseLiveData.value = featureEngineData

                    val topAdsRequestParams = getTopAdsRequestParams(it.engineData)
                    if (topAdsRequestParams != null) {
                        loadTopAdsViewModelData(topAdsRequestParams, thanksPageData)
                    }
                    postGyroRecommendation(it.engineData)
                }
            }
        }
    }

    @VisibleForTesting
     fun loadTopAdsViewModelData(
        topAdsRequestParams: TopAdsRequestParams,
        thanksPageData: ThanksPageData
    ) {
        thankYouTopAdsViewModelUseCase.cancelJobs()
        thankYouTopAdsViewModelUseCase.getTopAdsData(topAdsRequestParams, thanksPageData, {
            if (it.isNotEmpty()) {
                topAdsRequestParams.topAdsUIModelList = it
                _topAdsDataLiveData.postValue(topAdsRequestParams)
            }
        }, { it.printStackTrace() })
    }

    private fun getTopAdsRequestParams(engineData: FeatureEngineData?): TopAdsRequestParams? {
        return FeatureRecommendationMapper.getTopAdsParams(engineData)
    }

    @VisibleForTesting
    fun postGyroRecommendation(engineData: FeatureEngineData?) {
        gyroEngineMapperUseCase.cancelJobs()
        gyroEngineMapperUseCase.getFeatureListData(engineData, {
            _gyroRecommendationLiveData.postValue(it)
        }, { it.printStackTrace() })
    }

    private fun onThanksPageDataSuccess(thanksPageData: ThanksPageData) {
        thanksPageMapperUseCase.cancelJobs()
        thanksPageMapperUseCase.populateThanksPageDataFields(thanksPageData, {
            _thanksPageDataResultLiveData.postValue(Success(it))
        }, {
            _thanksPageDataResultLiveData.postValue(Fail(it))
        })
    }

    private fun onThanksPageDataError(throwable: Throwable) {
        _thanksPageDataResultLiveData.value = Fail(throwable)
    }

    fun getThanksPageTicker(configList: String?) {
        topTickerDataUseCase.getTopTickerData(configList, {
            _topTickerLiveData.postValue(Success(it))
        }, {
            _topTickerLiveData.postValue(Fail(it))
        })
    }

    fun resetAddressToDefault() {
        getDefaultAddressUseCase.getDefaultChosenAddress({
            _defaultAddressLiveData.postValue(Success(it))
        }, {
            _defaultAddressLiveData.postValue(Fail(it))
        })
    }

    override fun onCleared() {
        topTickerDataUseCase.cancelJobs()
        thanksPageDataUseCase.cancelJobs()
        gyroEngineRequestUseCase.cancelJobs()
        thankYouTopAdsViewModelUseCase.cancelJobs()
        thanksPageMapperUseCase.cancelJobs()
        gyroEngineMapperUseCase.cancelJobs()
        fetchWalletBalanceUseCase.cancelJobs()
        super.onCleared()
    }

}