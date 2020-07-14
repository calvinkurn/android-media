package com.tokopedia.sellerorder.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.domain.model.SomGetUserRoleDataModel
import com.tokopedia.sellerorder.common.domain.usecase.SomGetUserRoleUseCase
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.domain.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
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
                                             private val getUserRoleUseCase: SomGetUserRoleUseCase) : BaseViewModel(dispatcher.ui()) {

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

    private val _userRoleResult = MutableLiveData<Result<SomGetUserRoleDataModel>>()
    val userRoleResult: LiveData<Result<SomGetUserRoleDataModel>>
        get() = _userRoleResult

    fun loadDetailOrder(detailQuery: String, orderId: String) {
        launch {
            _orderDetailResult.postValue(somGetOrderDetailUseCase.execute(detailQuery, orderId))
        }
    }

    fun acceptOrder(acceptOrderQuery: String, orderId: String, shopId: String) {
        launch {
            _acceptOrderResult.postValue(somAcceptOrderUseCase.execute(acceptOrderQuery, orderId, shopId))
        }
    }

    fun getRejectReasons(rejectReasonQuery: String) {
        launch {
            _rejectReasonResult.postValue(somReasonRejectUseCase.execute(rejectReasonQuery, SomReasonRejectParam()))
        }
    }

    fun rejectOrder(rejectOrderQuery: String, rejectOrderRequest: SomRejectRequest) {
        launch {
            _rejectOrderResult.postValue(somRejectOrderUseCase.execute(rejectOrderQuery, rejectOrderRequest))
        }
    }

    fun editAwb(queryString: String) {
        launch {
            _editRefNumResult.postValue(somEditRefNumUseCase.execute(queryString))
        }
    }

    fun setDelivered(rawQuery: String, orderId: String, receivedBy: String) {
        launch {
            _setDelivered.postValue(somSetDeliveredUseCase.execute(rawQuery, orderId, receivedBy))
        }
    }

    fun loadUserRoles(userId: Int) {
        launchCatchError(block = {
            getUserRoleUseCase.setUserId(userId)
            _userRoleResult.postValue(getUserRoleUseCase.execute())
        }, onError = {
            _userRoleResult.postValue(Fail(it))
        })
    }

    fun setUserRoles(userRoles: SomGetUserRoleDataModel) {
        _userRoleResult.postValue(Success(userRoles))
    }
}