package com.tokopedia.buyerorder.detail.revamp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationDigiPersoResponse
import com.tokopedia.buyerorder.detail.domain.DigiPersoUseCase
import com.tokopedia.buyerorder.detail.domain.OmsDetailUseCase
import com.tokopedia.buyerorder.detail.domain.RevampActionButtonUseCase
import com.tokopedia.buyerorder.detail.view.adapter.ItemsAdapter
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * created by @bayazidnasir on 19/8/2022
 */

class OrderDetailViewModel @Inject constructor(
    private val omsDetailUseCase: Lazy<OmsDetailUseCase>,
    private val digiPersoUseCase: Lazy<DigiPersoUseCase>,
    private val actionButtonUseCase: Lazy<RevampActionButtonUseCase>,
    dispatcher: CoroutineDispatcher,
): BaseViewModel(dispatcher) {

    private val _omsDetail = MutableLiveData<Result<DetailsData>>()
    val omsDetail: LiveData<Result<DetailsData>>
        get() = _omsDetail

    private val _digiPerso = MutableLiveData<Result<RecommendationDigiPersoResponse>>()
    val digiPerso: LiveData<Result<RecommendationDigiPersoResponse>>
        get() = _digiPerso

    private val _actionButton = MutableLiveData<Result<Pair<Int, List<ActionButton>>>>()
    val actionButton: LiveData<Result<Pair<Int, List<ActionButton>>>>
        get() = _actionButton

    fun requestOmsDetail(orderId: String, orderCategory: String, upstream: String?){
        launchCatchError(
            block = {
                omsDetailUseCase.get().createParams(orderId, orderCategory, upstream)
                val result = omsDetailUseCase.get().executeOnBackground()
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
                digiPersoUseCase.get().createParams()
                val result = digiPersoUseCase.get().executeOnBackground()
                _digiPerso.postValue(Success(result))
            },
            onError = {
                _digiPerso.postValue(Fail(it))
            }
        )
    }

    fun requestActionButton(actionButton: List<ActionButton>, position: Int, flag: Boolean){
        launchCatchError(
            block = {
                actionButtonUseCase.get().setParams(actionButton)
                val result = actionButtonUseCase.get().executeOnBackground()

                if (flag){
                    result.actionButtonList.forEachIndexed { index, it ->
                        if (it.control.equals(ItemsAdapter.KEY_REFRESH, false)){
                            it.body = actionButton[index].body
                        }
                    }
                }
                _actionButton.postValue(Success(Pair(position, result.actionButtonList)))
            },
            onError = {
                _actionButton.postValue(Fail(it))
            }
        )
    }

}