package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.response.DefaultChosenAddressData
import com.tokopedia.thankyou_native.data.mapper.FeatureRecommendationMapper
import com.tokopedia.thankyou_native.data.mapper.PaymentDeductionKey
import com.tokopedia.thankyou_native.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.usecase.GetDefaultAddressUseCase
import com.tokopedia.thankyou_native.domain.usecase.GyroEngineRequestUseCase
import com.tokopedia.thankyou_native.domain.usecase.ThanksPageDataUseCase
import com.tokopedia.thankyou_native.domain.usecase.TopTickerUseCase
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ThanksPageDataViewModel @Inject constructor(
        private val thanksPageDataUseCase: ThanksPageDataUseCase,
        private val gyroEngineRequestUseCase: GyroEngineRequestUseCase,
        private val topTickerDataUseCase: TopTickerUseCase,
        private val getDefaultAddressUseCase: GetDefaultAddressUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
        @CoroutineBackgroundDispatcher val dispatcherIO: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val thanksPageDataResultLiveData = MutableLiveData<Result<ThanksPageData>>()
    val gyroRecommendationLiveData = MutableLiveData<GyroRecommendation>()
    val topTickerLiveData = MutableLiveData<Result<List<TickerData>>>()
    val defaultAddressLiveData = MutableLiveData<Result<DefaultChosenAddressData>>()

    fun getThanksPageData(paymentId: Long, merchant: String) {
        thanksPageDataUseCase.cancelJobs()
        thanksPageDataUseCase.getThankPageData(
                ::onThanksPageDataSuccess,
                ::onThanksPageDataError,
                paymentId,
                merchant
        )
    }

    fun getFeatureEngine(thanksPageData: ThanksPageData) {
        gyroEngineRequestUseCase.cancelJobs()
        gyroEngineRequestUseCase.getFeatureEngineData(
                thanksPageData
        ) {
            if (it.success) {
                postGyroRecommendation(it.engineData)
            }
        }
    }

    private fun postGyroRecommendation(engineData: FeatureEngineData?) {
        launchCatchError(block = {
            val gyroRecommendation: GyroRecommendation? = withContext(dispatcherIO) {
                return@withContext FeatureRecommendationMapper.getFeatureList(engineData)
            }
            gyroRecommendation?.let {
                gyroRecommendationLiveData.postValue(gyroRecommendation)
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun onThanksPageDataSuccess(thanksPageData: ThanksPageData) {
        launchCatchError(block = {
            withContext(dispatcherIO) {
                thanksPageData.paymentDeductions?.forEach {
                    if (it.itemName == PaymentDeductionKey.REWARDS_POINT) {
                        thanksPageData.paymentMethodCount++
                    }
                }
                thanksPageData.paymentDetails?.apply {
                    thanksPageData.paymentMethodCount += (size - 1)
                }
            }
            thanksPageDataResultLiveData.postValue(Success(thanksPageData))
        }, onError = {
            thanksPageDataResultLiveData.postValue(Fail(it))
        })
    }

    private fun onThanksPageDataError(throwable: Throwable) {
        thanksPageDataResultLiveData.value = Fail(throwable)
    }

    fun getThanksPageTicker(configList: String?) {
        topTickerDataUseCase.getTopTickerData(configList, {
            topTickerLiveData.postValue(Success(it))
        }, {
            topTickerLiveData.postValue(Fail(it))
        })
    }

    fun resetAddressToDefault(){
        getDefaultAddressUseCase.getDefaultChosenAddress( {
            defaultAddressLiveData.postValue(Success(it))
        },{
            defaultAddressLiveData.postValue(Fail(it))
        } )
    }

    override fun onCleared() {
        topTickerDataUseCase.cancelJobs()
        thanksPageDataUseCase.cancelJobs()
        gyroEngineRequestUseCase.cancelJobs()
        super.onCleared()
    }

}