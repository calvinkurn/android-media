package com.tokopedia.sellerorder.common.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.common.domain.model.*
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async

abstract class SomOrderBaseViewModel(
        dispatcher: CoroutineDispatchers,
        protected val userSession: UserSessionInterface,
        private val somAcceptOrderUseCase: SomAcceptOrderUseCase,
        private val somRejectOrderUseCase: SomRejectOrderUseCase,
        private val somEditRefNumUseCase: SomEditRefNumUseCase,
        private val somRejectCancelOrderRequest: SomRejectCancelOrderUseCase,
        private val somValidateOrderUseCase: SomValidateOrderUseCase,
        private val firstAuthorizeAccessUseCase: AuthorizeAccessUseCase,
        private val secondAuthorizeAccessUseCase: AuthorizeAccessUseCase): BaseViewModel(dispatcher.io) {

    private val _acceptOrderResult = MutableLiveData<Result<SomAcceptOrderResponse.Data>>()
    val acceptOrderResult: LiveData<Result<SomAcceptOrderResponse.Data>>
        get() = _acceptOrderResult

    private val _rejectOrderResult = MutableLiveData<Result<SomRejectOrderResponse.Data>>()
    val rejectOrderResult: LiveData<Result<SomRejectOrderResponse.Data>>
        get() = _rejectOrderResult

    private val _editRefNumResult = MutableLiveData<Result<SomEditRefNumResponse.Data>>()
    val editRefNumResult: LiveData<Result<SomEditRefNumResponse.Data>>
        get() = _editRefNumResult

    private val _rejectCancelOrderResult = MutableLiveData<Result<SomRejectCancelOrderResponse.Data>>()
    val rejectCancelOrderResult: LiveData<Result<SomRejectCancelOrderResponse.Data>>
        get() = _rejectCancelOrderResult

    private val _validateOrderResult = MutableLiveData<Result<Boolean>>()
    val validateOrderResult: LiveData<Result<Boolean>>
        get() = _validateOrderResult

    protected open suspend fun doAcceptOrder(orderId: String, invoice: String) {
        somAcceptOrderUseCase.setParams(orderId, userSession.shopId ?: "0")
        _acceptOrderResult.postValue(Success(somAcceptOrderUseCase.execute()))
    }

    protected open suspend fun doRejectOrder(rejectOrderRequestParam: SomRejectRequestParam, invoice: String) {
        _rejectOrderResult.postValue(Success(somRejectOrderUseCase.execute(rejectOrderRequestParam)))
    }

    protected open suspend fun doEditAwb(orderId: String, shippingRef: String, invoice: String) {
        somEditRefNumUseCase.setParams(SomEditRefNumRequestParam(orderId, shippingRef))
        _editRefNumResult.postValue(Success(somEditRefNumUseCase.execute()))
    }

    protected open suspend fun doRejectCancelOrder(orderId: String, invoice: String) {
        _rejectCancelOrderResult.postValue(Success(somRejectCancelOrderRequest.execute(SomRejectCancelOrderRequest(orderId))))
    }

    fun acceptOrder(orderId: String, invoice: String = "") {
        launchCatchError(block = {
            doAcceptOrder(orderId, invoice)
        }, onError = {
            _acceptOrderResult.postValue(Fail(it))
        })
    }

    fun rejectOrder(rejectOrderRequestParam: SomRejectRequestParam, invoice: String = "") {
        launchCatchError(block = {
            doRejectOrder(rejectOrderRequestParam, invoice)
        }, onError = {
            _rejectOrderResult.postValue(Fail(it))
        })
    }

    fun editAwb(orderId: String, shippingRef: String, invoice: String = "") {
        launchCatchError(block = {
            doEditAwb(orderId, shippingRef, invoice)
        }, onError = {
            _editRefNumResult.postValue(Fail(it))
        })
    }

    fun rejectCancelOrder(orderId: String, invoice: String = "") {
        launchCatchError(block = {
            doRejectCancelOrder(orderId, invoice)
        }, onError = { _rejectCancelOrderResult.postValue(Fail(it)) })
    }

    fun validateOrders(orderIds: List<String>) {
        launchCatchError(block = {
            val params = SomValidateOrderRequest(orderIds)
            val result = somValidateOrderUseCase.execute(params)
            _validateOrderResult.postValue(Success(result))
        }, onError = {
            _validateOrderResult.postValue(Fail(it))
        })
    }

    protected suspend fun getAdminAccessEligibilityPair(@AccessId firstAccessId: Int,
                                                        @AccessId secondAccessId: Int): Result<Pair<Boolean, Boolean>> {
        return if (userSession.isShopOwner) {
            Success(true to true)
        } else {
            userSession.shopId.toLongOrZero().let { shopId ->
                val firstEligibilityDeferred = async {
                    AuthorizeAccessUseCase.createRequestParams(shopId, firstAccessId).let { requestParam ->
                        firstAuthorizeAccessUseCase.execute(requestParam)
                    }
                }
                val secondEligibilityDeferred = async {
                    AuthorizeAccessUseCase.createRequestParams(shopId, secondAccessId).let { requestParam ->
                        secondAuthorizeAccessUseCase.execute(requestParam)
                    }
                }
                Success(firstEligibilityDeferred.await() to secondEligibilityDeferred.await())
            }
        }
    }
}