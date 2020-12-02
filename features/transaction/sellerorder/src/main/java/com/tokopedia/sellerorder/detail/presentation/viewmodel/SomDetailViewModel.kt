package com.tokopedia.sellerorder.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.domain.SomGetOrderDetailUseCase
import com.tokopedia.sellerorder.detail.domain.SomReasonRejectUseCase
import com.tokopedia.sellerorder.detail.domain.SomSetDeliveredUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-09-30.
 */
class SomDetailViewModel @Inject constructor(
        dispatcher: SomDispatcherProvider,
        userSession: UserSessionInterface,
        private val somGetOrderDetailUseCase: SomGetOrderDetailUseCase,
        somAcceptOrderUseCase: SomAcceptOrderUseCase,
        private val somReasonRejectUseCase: SomReasonRejectUseCase,
        somRejectOrderUseCase: SomRejectOrderUseCase,
        somEditRefNumUseCase: SomEditRefNumUseCase,
        private val somSetDeliveredUseCase: SomSetDeliveredUseCase,
        private val getUserRoleUseCase: SomGetUserRoleUseCase,
        somRejectCancelOrderRequest: SomRejectCancelOrderUseCase
) : SomOrderBaseViewModel(dispatcher.ui(), userSession, somAcceptOrderUseCase, somRejectOrderUseCase,
        somEditRefNumUseCase, somRejectCancelOrderRequest, getUserRoleUseCase) {

    private val _orderDetailResult = MutableLiveData<Result<GetSomDetailResponse>>()
    val orderDetailResult: LiveData<Result<GetSomDetailResponse>>
        get() = _orderDetailResult

    private val _rejectReasonResult = MutableLiveData<Result<SomReasonRejectData.Data>>()
    val rejectReasonResult: LiveData<Result<SomReasonRejectData.Data>>
        get() = _rejectReasonResult

    private val _setDelivered = MutableLiveData<Result<SetDeliveredResponse>>()
    val setDelivered: LiveData<Result<SetDeliveredResponse>>
        get() = _setDelivered

    fun loadDetailOrder(orderId: String) {
        launchCatchError(block = {
            val dynamicPriceParam = SomDynamicPriceRequest(order_id = orderId.toIntOrNull() ?: 0)
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

    fun loadUserRoles(userId: Int) {
        launchCatchError(block = {
            getUserRoleUseCase.setUserId(userId)
            _userRoleResult.postValue(getUserRoleUseCase.execute())
        }, onError = {
            _userRoleResult.postValue(Fail(it))
        })
    }
}