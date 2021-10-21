package com.tokopedia.sellerorder.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.domain.SomGetOrderDetailUseCase
import com.tokopedia.sellerorder.detail.domain.SomReasonRejectUseCase
import com.tokopedia.sellerorder.detail.domain.SomSetDeliveredUseCase
import com.tokopedia.sellerorder.orderextension.domain.usecases.*
import com.tokopedia.sellerorder.orderextension.presentation.mapper.GetOrderExtensionRequestInfoResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.mapper.OrderExtensionRequestResultResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUpdater
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestResultUiModel
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val dispatcher: CoroutineDispatchers,
    private val somGetOrderDetailUseCase: SomGetOrderDetailUseCase,
    private val somReasonRejectUseCase: SomReasonRejectUseCase,
    private val somSetDeliveredUseCase: SomSetDeliveredUseCase,
    private val somGetOrderExtensionRequestInfoUseCase: GetOrderExtensionRequestInfoUseCase,
    private val somSendOrderRequestExtensionUseCase: SendOrderExtensionRequestUseCase,
    private val somGetOrderExtensionRequestInfoMapper: GetOrderExtensionRequestInfoResponseMapper,
    private val somOrderExtensionRequestResultMapper: OrderExtensionRequestResultResponseMapper,
    authorizeSomDetailAccessUseCase: AuthorizeAccessUseCase,
    authorizeReplyChatAccessUseCase: AuthorizeAccessUseCase
) : SomOrderBaseViewModel(
    dispatcher, userSession, somAcceptOrderUseCase, somRejectOrderUseCase,
    somEditRefNumUseCase, somRejectCancelOrderRequest, somValidateOrderUseCase,
    authorizeSomDetailAccessUseCase, authorizeReplyChatAccessUseCase
) {

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

    private val _requestExtensionInfo = MutableLiveData<Result<OrderExtensionRequestInfoUiModel>>()
    val requestExtensionInfo: LiveData<Result<OrderExtensionRequestInfoUiModel>>
        get() = _requestExtensionInfo

    private val _requestExtensionResult = MutableLiveData<Result<OrderExtensionRequestResultUiModel>>()
    val requestExtensionResult: LiveData<Result<OrderExtensionRequestResultUiModel>>
        get() = _requestExtensionResult

    private val orderExtensionRequestInfoUpdates: MutableLiveData<OrderExtensionRequestInfoUpdater> = MutableLiveData()

    private var loadDetailJob: Job? = null

    init {
        launch {
            orderExtensionRequestInfoUpdates
                .asFlow()
                .flowOn(dispatcher.computation)
                .collect { updater ->
                    _requestExtensionInfo.value?.let { oldRequestExtensionInfo ->
                        if (oldRequestExtensionInfo is Success) {
                            updater.execute(oldRequestExtensionInfo.data)
                                .also { newRequestExtensionInfo ->
                                    withContext(dispatcher.main) {
                                        _requestExtensionInfo.value = Success(newRequestExtensionInfo)
                                    }
                                }
                        }
                    }
                }
        }
    }

    private fun startSendingOrderExtensionRequest(action: (OrderExtensionRequestInfoUiModel) -> Unit) {
        orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater.OnStartSendingOrderExtensionRequest(action)
    }

    private suspend fun onFailedSendingOrderExtensionRequest() {
        withContext(dispatcher.main) {
            orderExtensionRequestInfoUpdates.value =
                OrderExtensionRequestInfoUpdater.OnFinishSendingOrderExtensionRequest()
        }
    }

    fun loadDetailOrder(orderId: String) {
        loadDetailJob?.cancel()
        loadDetailJob = launchCatchError(block = {
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

    fun getSomRequestExtensionInfo(orderId: String) {
        launchCatchError(block = {
            val result = somGetOrderExtensionRequestInfoUseCase.execute(orderId, userSession.shopId)
            _requestExtensionInfo.postValue(Success(somGetOrderExtensionRequestInfoMapper.mapSuccessResponseToUiModel(result)))
        }, onError = {
            _requestExtensionInfo.postValue(Fail(it))
        })
    }

    fun sendOrderExtensionRequest(orderId: String) {
        startSendingOrderExtensionRequest { requestExtensionInfo ->
            launchCatchError(block = {
                if (requestExtensionInfo.isValid()) {
                    val selectedOptionCode = requestExtensionInfo.getSelectedOptionCode()
                    val result = somSendOrderRequestExtensionUseCase.execute(
                        userSession.userId,
                        orderId,
                        userSession.shopId,
                        selectedOptionCode,
                        requestExtensionInfo.getComment(selectedOptionCode)
                    )
                    val mappedResult = somOrderExtensionRequestResultMapper.mapResponseToUiModel(result)
                    _requestExtensionResult.postValue(Success(mappedResult))
                    if (!mappedResult.success) {
                        onFailedSendingOrderExtensionRequest()
                    }
                } else {
                    onFailedSendingOrderExtensionRequest()
                }
            }, onError = {
                _requestExtensionResult.postValue(Fail(it))
                onFailedSendingOrderExtensionRequest()
            })
        }
    }

    fun updateOrderRequestExtensionInfoOnCommentChanged(element: OrderExtensionRequestInfoUiModel.CommentUiModel?) {
        element?.let {
            orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater.OnCommentChange(it)
        }
    }

    fun updateOrderRequestExtensionInfoOnSelectedOptionChanged(element: OrderExtensionRequestInfoUiModel.OptionUiModel?) {
        element?.let {
            orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater.OnSelectedOptionChange(it)
        }
    }
}