package com.tokopedia.sellerorder.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.domain.usecase.SomGetUserRoleUseCase
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.domain.*
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

    private val _acceptOrderResult = MutableLiveData<Result<SomAcceptOrder.Data>>()
    val acceptOrderResult: LiveData<Result<SomAcceptOrder.Data>>
        get() = _acceptOrderResult

    private val _rejectReasonResult = MutableLiveData<Result<SomReasonRejectData.Data>>()
    val rejectReasonResult: LiveData<Result<SomReasonRejectData.Data>>
        get() = _rejectReasonResult

    private val _rejectOrderResult = MutableLiveData<Result<SomRejectOrder.Data>>()
    val rejectOrderResult: LiveData<Result<SomRejectOrder.Data>>
        get() = _rejectOrderResult

    private val _editRefNumResult = MutableLiveData<Result<SomEditAwbResponse.Data>>()
    val editRefNumResult: LiveData<Result<SomEditAwbResponse.Data>>
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

    fun loadDetailOrder(detailQuery: String, orderId: String) {
        launchCatchError(block = {
            _orderDetailResult.postValue(somGetOrderDetailUseCase.execute(detailQuery, orderId))
        }, onError = {
            _orderDetailResult.postValue(Fail(it))
        })
    }

    fun acceptOrder(acceptOrderQuery: String, orderId: String, shopId: String) {
        launchCatchError(block = {
            _acceptOrderResult.postValue(somAcceptOrderUseCase.execute(acceptOrderQuery, orderId, shopId))
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

    fun rejectOrder(rejectOrderQuery: String, rejectOrderRequest: SomRejectRequest) {
        launchCatchError(block = {
            _rejectOrderResult.postValue(somRejectOrderUseCase.execute(rejectOrderQuery, rejectOrderRequest))
        }, onError = {
            _rejectOrderResult.postValue(Fail(it))
        })
    }

    fun editAwb(queryString: String) {
        launchCatchError(block = {
            _editRefNumResult.postValue(somEditRefNumUseCase.execute(queryString))
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