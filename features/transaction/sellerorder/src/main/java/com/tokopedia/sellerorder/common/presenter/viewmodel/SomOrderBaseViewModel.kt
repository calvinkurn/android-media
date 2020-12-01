package com.tokopedia.sellerorder.common.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerorder.common.domain.model.*
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job

abstract class SomOrderBaseViewModel constructor(
        dispatcher: CoroutineDispatcher,
        protected val userSession: UserSessionInterface,
        private val somAcceptOrderUseCase: SomAcceptOrderUseCase,
        private val somRejectOrderUseCase: SomRejectOrderUseCase,
        private val somEditRefNumUseCase: SomEditRefNumUseCase,
        private val somRejectCancelOrderRequest: SomRejectCancelOrderUseCase,
        private val getUserRoleUseCase: SomGetUserRoleUseCase): BaseViewModel(dispatcher) {

    private val _acceptOrderResult = MutableLiveData<Result<SomAcceptOrderResponse.Data>>()
    val acceptOrderResult: LiveData<Result<SomAcceptOrderResponse.Data>>
        get() = _acceptOrderResult

    private val _rejectOrderResult = MutableLiveData<Result<SomRejectOrderResponse.Data>>()
    val rejectOrderResult: LiveData<Result<SomRejectOrderResponse.Data>>
        get() = _rejectOrderResult

    private val _editRefNumResult = MutableLiveData<Result<SomEditRefNumResponse.Data>>()
    val editRefNumResult: LiveData<Result<SomEditRefNumResponse.Data>>
        get() = _editRefNumResult

    protected val _userRoleResult = MutableLiveData<Result<SomGetUserRoleUiModel>>()
    val userRoleResult: LiveData<Result<SomGetUserRoleUiModel>>
        get() = _userRoleResult

    private val _rejectCancelOrderResult = MutableLiveData<Result<SomRejectCancelOrderResponse.Data>>()
    val rejectCancelOrderResult: LiveData<Result<SomRejectCancelOrderResponse.Data>>
        get() = _rejectCancelOrderResult

    private var userRolesJob: Job? = null

    fun acceptOrder(orderId: String) {
        launchCatchError(block = {
            somAcceptOrderUseCase.setParams(orderId, userSession.shopId ?: "0")
            _acceptOrderResult.postValue(somAcceptOrderUseCase.execute())
        }, onError = {
            _acceptOrderResult.postValue(Fail(it))
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

    fun rejectCancelOrder(orderId: String) {
        launchCatchError(block = {
            _rejectCancelOrderResult.postValue(somRejectCancelOrderRequest.execute(SomRejectCancelOrderRequest(orderId)))
        }, onError = { _rejectCancelOrderResult.postValue(Fail(it)) })
    }

    fun getUserRoles() {
        if (getUserRolesJob()?.isCompleted != false) {
            setUserRolesJob(launchCatchError(block = {
                getUserRoleUseCase.setUserId(userSession.userId.toIntOrZero())
                _userRoleResult.postValue(getUserRoleUseCase.execute())
            }, onError = {
                _userRoleResult.postValue(Fail(it))
            }))
        }
    }

    fun setUserRoles(userRoles: SomGetUserRoleUiModel) {
        _userRoleResult.postValue(Success(userRoles))
    }

    fun getUserRolesJob() = userRolesJob

    fun setUserRolesJob(job: Job) {
        userRolesJob = job
    }
}