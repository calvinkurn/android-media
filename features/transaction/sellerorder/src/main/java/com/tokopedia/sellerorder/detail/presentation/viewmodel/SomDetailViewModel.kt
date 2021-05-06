package com.tokopedia.sellerorder.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.domain.SomGetOrderDetailUseCase
import com.tokopedia.sellerorder.detail.domain.SomReasonRejectUseCase
import com.tokopedia.sellerorder.detail.domain.SomSetDeliveredUseCase
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-09-30.
 */
class SomDetailViewModel @Inject constructor(
        somAcceptOrderUseCase: SomAcceptOrderUseCase,
        somRejectOrderUseCase: SomRejectOrderUseCase,
        somEditRefNumUseCase: SomEditRefNumUseCase,
        somRejectCancelOrderRequest: SomRejectCancelOrderUseCase,
        somValidateOrderUseCase: SomValidateOrderUseCase,
        userSession: UserSessionInterface,
        dispatcher: CoroutineDispatchers,
        private val somGetOrderDetailUseCase: SomGetOrderDetailUseCase,
        private val somReasonRejectUseCase: SomReasonRejectUseCase,
        private val somSetDeliveredUseCase: SomSetDeliveredUseCase,
        authorizeSomDetailAccessUseCase: AuthorizeAccessUseCase,
        authorizeReplyChatAccessUseCase: AuthorizeAccessUseCase
) : SomOrderBaseViewModel(dispatcher, userSession, somAcceptOrderUseCase, somRejectOrderUseCase,
        somEditRefNumUseCase, somRejectCancelOrderRequest, somValidateOrderUseCase,
        authorizeSomDetailAccessUseCase, authorizeReplyChatAccessUseCase) {

    private val _orderDetailResult = MutableLiveData<Result<GetSomDetailResponse>>()
    val orderDetailResult: LiveData<Result<GetSomDetailResponse>>
        get() = _orderDetailResult

    private val _rejectReasonResult = MutableLiveData<Result<SomReasonRejectData.Data>>()
    val rejectReasonResult: LiveData<Result<SomReasonRejectData.Data>>
        get() = _rejectReasonResult

    private val _setDelivered = MutableLiveData<Result<SetDeliveredResponse>>()
    val setDelivered: LiveData<Result<SetDeliveredResponse>>
        get() = _setDelivered

    private val _somDetailChatEligibility = MutableLiveData<Result<Pair<Boolean, Boolean>>>()
    val somDetailChatEligibility: LiveData<Result<Pair<Boolean, Boolean>>>
        get() = _somDetailChatEligibility

    private var loadDetailJob: Job? = null

    fun loadDetailOrder(orderId: String) {
        loadDetailJob?.cancel()
        loadDetailJob = launchCatchError(block = {
            val dynamicPriceParam = SomDynamicPriceRequest(order_id = orderId.toLongOrZero())
            somGetOrderDetailUseCase.setParamDynamicPrice(dynamicPriceParam)
            val somGetOrderDetail = somGetOrderDetailUseCase.execute(orderId)
            _orderDetailResult.postValue(somGetOrderDetail)
        }, onError = {
            _orderDetailResult.postValue(Fail(it))
        })
    }

    fun getRejectReasons(rejectReasonQuery: String) {
        launchCatchError(block = {
            _rejectReasonResult.postValue(somReasonRejectUseCase.execute(rejectReasonQuery, SomReasonRejectParam()))
        }, onError = {
            _rejectReasonResult.postValue(Fail(it))
        })
    }

    fun setDelivered(rawQuery: String, orderId: String, receivedBy: String) {
        launchCatchError(block = {
            _setDelivered.postValue(somSetDeliveredUseCase.execute(rawQuery, orderId, receivedBy))
        }, onError = {
            _setDelivered.postValue(Fail(it))
        })
    }
    fun getAdminPermission() {
        launchCatchError(
                block = {
                    _somDetailChatEligibility.postValue(getAdminAccessEligibilityPair(AccessId.SOM_DETAIL, AccessId.CHAT_REPLY))
                },
                onError = {
                    _somDetailChatEligibility.postValue(Fail(it))
                }
        )
    }
}