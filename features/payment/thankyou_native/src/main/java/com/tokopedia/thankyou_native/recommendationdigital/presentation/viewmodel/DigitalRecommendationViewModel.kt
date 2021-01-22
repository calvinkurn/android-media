package com.tokopedia.thankyou_native.recommendationdigital.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.thankyou_native.recommendationdigital.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.recommendationdigital.domain.usecase.DigitalRecommendationUseCase
import com.tokopedia.thankyou_native.recommendationdigital.model.DigitalRecommendationList
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DigitalRecommendationViewModel @Inject constructor(
        private val digitalRecommendationUseCase: DigitalRecommendationUseCase,
         @CoroutineMainDispatcher dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val digitalRecommendationLiveData = MutableLiveData<Result<DigitalRecommendationList>>()

    fun getDigitalRecommendationData(deviceId: Int, categoryId: String) {
        digitalRecommendationUseCase.cancelJobs()
        digitalRecommendationUseCase.getDigitalRecommendationData(
                ::onDigitalRecomDataSuccess,
                ::onDigitalRecomError,
                deviceId,
                categoryId
        )
    }

    private fun onDigitalRecomDataSuccess(digitalRecommendationList: DigitalRecommendationList) {
        digitalRecommendationLiveData.value = Success(digitalRecommendationList)
    }

    private fun onDigitalRecomError(throwable: Throwable) {
        digitalRecommendationLiveData.value = Fail(throwable)
    }

    override fun onCleared() {
        digitalRecommendationUseCase.cancelJobs()
        super.onCleared()
    }


}