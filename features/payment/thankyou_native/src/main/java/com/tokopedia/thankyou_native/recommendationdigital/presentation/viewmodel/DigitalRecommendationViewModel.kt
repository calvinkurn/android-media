package com.tokopedia.thankyou_native.recommendationdigital.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.thankyou_native.data.mapper.ThankPageType
import com.tokopedia.thankyou_native.recommendationdigital.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.recommendationdigital.domain.usecase.DigitalRecommendationUseCase
import com.tokopedia.thankyou_native.recommendationdigital.model.RechargeRecommendationDigiPersoItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DigitalRecommendationViewModel @Inject constructor(
        private val digitalRecommendationUseCase: DigitalRecommendationUseCase,
         @CoroutineMainDispatcher dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    private val _digitalRecommendationLiveData = MutableLiveData<Result<RechargeRecommendationDigiPersoItem>>()
    val digitalRecommendationLiveData: LiveData<Result<RechargeRecommendationDigiPersoItem>> = _digitalRecommendationLiveData

    fun getDigitalRecommendationData(clientNumber: String, pgCategoryIds: List<Int>, pageType: ThankPageType) {
        digitalRecommendationUseCase.cancelJobs()
        digitalRecommendationUseCase.getDigitalRecommendationData(
                ::onDigitalRecomDataSuccess,
                ::onDigitalRecomError,
                clientNumber,
                pgCategoryIds,
                pageType
        )
    }

    private fun onDigitalRecomDataSuccess(digitalRecommendationList: RechargeRecommendationDigiPersoItem) {
        _digitalRecommendationLiveData.value = Success(digitalRecommendationList)
    }

    private fun onDigitalRecomError(throwable: Throwable) {
        _digitalRecommendationLiveData.value = Fail(throwable)
    }

    override fun onCleared() {
        digitalRecommendationUseCase.cancelJobs()
        super.onCleared()
    }


}