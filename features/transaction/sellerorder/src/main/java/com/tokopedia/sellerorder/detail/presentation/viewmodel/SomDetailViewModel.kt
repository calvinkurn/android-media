package com.tokopedia.sellerorder.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.domain.model.*
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.detail.data.model.SetDeliveredResponse
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectParam
import com.tokopedia.sellerorder.detail.domain.SomGetOrderDetailUseCase
import com.tokopedia.sellerorder.detail.domain.SomReasonRejectUseCase
import com.tokopedia.sellerorder.detail.domain.SomSetDeliveredUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-09-30.
 */
class SomDetailViewModel @Inject constructor(dispatcher: SomDispatcherProvider,
                                             private val somGetOrderDetailUseCase: SomGetOrderDetailUseCase,
                                             private val somAcceptOrderUseCase: SomAcceptOrderUseCase,
                                             private val somReasonRejectUseCase: SomReasonRejectUseCase,
                                             private val somRejectOrderUseCase: SomRejectOrderUseCase,
                                             private val somEditRefNumUseCase: SomEditRefNumUseCase,
                                             private val somSetDeliveredUseCase: SomSetDeliveredUseCase,
                                             private val getUserRoleUseCase: SomGetUserRoleUseCase,
                                             private val somRejectCancelOrderRequest: SomRejectCancelOrderUseCase) : BaseViewModel(dispatcher.ui()) {

    private val _orderDetailResult = MutableLiveData<Result<SomDetailOrder.Data.GetSomDetail>>()
    val orderDetailResult: LiveData<Result<SomDetailOrder.Data.GetSomDetail>>
        get() = _orderDetailResult

    private val _acceptOrderResult = MutableLiveData<Result<SomAcceptOrderResponse.Data>>()
    val acceptOrderResult: LiveData<Result<SomAcceptOrderResponse.Data>>
        get() = _acceptOrderResult

    private val _rejectReasonResult = MutableLiveData<Result<SomReasonRejectData.Data>>()
    val rejectReasonResult: LiveData<Result<SomReasonRejectData.Data>>
        get() = _rejectReasonResult

    private val _rejectOrderResult = MutableLiveData<Result<SomRejectOrderResponse.Data>>()
    val rejectOrderResult: LiveData<Result<SomRejectOrderResponse.Data>>
        get() = _rejectOrderResult

    private val _editRefNumResult = MutableLiveData<Result<SomEditRefNumResponse.Data>>()
    val editRefNumResult: LiveData<Result<SomEditRefNumResponse.Data>>
        get() = _editRefNumResult

    private val _setDelivered = MutableLiveData<Result<SetDeliveredResponse>>()
    val setDelivered: LiveData<Result<SetDeliveredResponse>>
        get() = _setDelivered

    private val _userRoleResult = MutableLiveData<Result<SomGetUserRoleUiModel>>()
    val userRoleResult: LiveData<Result<SomGetUserRoleUiModel>>
        get() = _userRoleResult

    private val _rejectCancelOrderResult = MutableLiveData<Result<SomRejectCancelOrderResponse.Data>>()
    val rejectCancelOrderResult: LiveData<Result<SomRejectCancelOrderResponse.Data>>
        get() = _rejectCancelOrderResult

    fun loadDetailOrder(orderId: String) {
        launchCatchError(block = {
            _orderDetailResult.postValue(somGetOrderDetailUseCase.execute(orderId))
        }, onError = {
            _orderDetailResult.postValue(Fail(it))
        })
    }

    fun acceptOrder(orderId: String, shopId: String) {
        launchCatchError(block = {
            somAcceptOrderUseCase.setParams(orderId, shopId)
            _acceptOrderResult.postValue(somAcceptOrderUseCase.execute())
        }, onError = {
            _acceptOrderResult.postValue(Fail(it))
        })
    }

    fun getRejectReasons(rejectReasonQuery: String) {
        launchCatchError(block = {
            _rejectReasonResult.postValue(somReasonRejectUseCase.execute(rejectReasonQuery, SomReasonRejectParam()))
        }, onError = {
            _rejectReasonResult.postValue(Fail(it))
        })
    }

    fun rejectOrder(rejectOrderRequestParam: SomRejectRequestParam) {
        launchCatchError(block = {
            _rejectOrderResult.postValue(somRejectOrderUseCase.execute(rejectOrderRequestParam))
        }, onError = {
            _rejectOrderResult.postValue(Fail(it))
        })
    }

    fun editAwb(orderId: String, shippingRef: String) {
        launchCatchError(block = {
            somEditRefNumUseCase.setParams(SomEditRefNumRequestParam(orderId, shippingRef))
            _editRefNumResult.postValue(somEditRefNumUseCase.execute())
        }, onError = {
            _editRefNumResult.postValue(Fail(it))
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

    fun setUserRoles(userRoles: SomGetUserRoleUiModel) {
        _userRoleResult.postValue(Success(userRoles))
    }

    fun rejectCancelOrder(orderId: String) {
        launchCatchError(block = {
            _rejectCancelOrderResult.postValue(
                    somRejectCancelOrderRequest.execute(SomRejectCancelOrderRequest(orderId))
            )
        }, onError = { _rejectCancelOrderResult.postValue(Fail(it)) })
    }
}