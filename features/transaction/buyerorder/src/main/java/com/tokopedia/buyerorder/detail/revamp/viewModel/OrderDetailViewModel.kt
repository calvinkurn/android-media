package com.tokopedia.buyerorder.detail.revamp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationDigiPersoResponse
import com.tokopedia.buyerorder.detail.domain.DigiPersoUseCase
import com.tokopedia.buyerorder.detail.domain.OmsDetailUseCase
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * created by @bayazidnasir on 19/8/2022
 */

class OrderDetailViewModel @Inject constructor(
    private val omsDetailUseCase: OmsDetailUseCase,
    private val digiPersoUseCase: DigiPersoUseCase,
    dispatcher: CoroutineDispatcher,
): BaseViewModel(dispatcher) {

    private val _omsDetail = MutableLiveData<Result<DetailsData>>()
    val omsDetail: LiveData<Result<DetailsData>>
        get() = _omsDetail

    private val _digiPerso = MutableLiveData<Result<RecommendationDigiPersoResponse>>()
    val digiPerso: LiveData<Result<RecommendationDigiPersoResponse>>
        get() = _digiPerso

    fun requestOmsDetail(orderId: String, orderCategory: String, upstream: String){
        launchCatchError(
            block = {
                omsDetailUseCase.createParams(orderId, orderCategory, upstream)
                val result = omsDetailUseCase.executeOnBackground()
                _omsDetail.postValue(Success(result))
            },
            onError = {
                _omsDetail.postValue(Fail(it))
            }
        )
    }

    fun requestDigiPerso(){
        launchCatchError(
            block = {
                digiPersoUseCase.createParams()
                val result = digiPersoUseCase.executeOnBackground()
                _digiPerso.postValue(Success(result))
            },
            onError = {
                _digiPerso.postValue(Fail(it))
            }
        )
    }

}