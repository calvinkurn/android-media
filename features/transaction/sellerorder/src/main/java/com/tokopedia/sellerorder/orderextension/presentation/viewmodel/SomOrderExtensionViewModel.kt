package com.tokopedia.sellerorder.orderextension.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.orderextension.domain.usecases.GetOrderExtensionRequestInfoUseCase
import com.tokopedia.sellerorder.orderextension.domain.usecases.SendOrderExtensionRequestUseCase
import com.tokopedia.sellerorder.orderextension.presentation.mapper.GetOrderExtensionRequestInfoResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.mapper.OrderExtensionRequestResultResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUpdater
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestResultUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SomOrderExtensionViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val somGetOrderExtensionRequestInfoUseCase: GetOrderExtensionRequestInfoUseCase,
    private val somSendOrderRequestExtensionUseCase: SendOrderExtensionRequestUseCase,
    private val somGetOrderExtensionRequestInfoMapper: GetOrderExtensionRequestInfoResponseMapper,
    private val somOrderExtensionRequestResultMapper: OrderExtensionRequestResultResponseMapper,
) : BaseViewModel(dispatcher.io) {
    private val _requestExtensionInfo = MutableLiveData<Result<OrderExtensionRequestInfoUiModel>>()
    val requestExtensionInfo: LiveData<Result<OrderExtensionRequestInfoUiModel>>
        get() = _requestExtensionInfo

    private val _requestExtensionResult = MutableLiveData<Result<OrderExtensionRequestResultUiModel>>()
    val requestExtensionResult: LiveData<Result<OrderExtensionRequestResultUiModel>>
        get() = _requestExtensionResult

    private val orderExtensionRequestInfoUpdates: MutableLiveData<OrderExtensionRequestInfoUpdater> = MutableLiveData()

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
                                        _requestExtensionInfo.value =
                                            Success(newRequestExtensionInfo)
                                    }
                                }
                        }
                    }
                }
        }
    }

    private fun startSendingOrderExtensionRequest(action: (OrderExtensionRequestInfoUiModel) -> Unit) {
        orderExtensionRequestInfoUpdates.value =
            OrderExtensionRequestInfoUpdater.OnStartSendingOrderExtensionRequest(action)
    }

    private suspend fun onFailedSendingOrderExtensionRequest(shouldDismiss: Boolean) {
        withContext(dispatcher.main) {
            orderExtensionRequestInfoUpdates.value =
                OrderExtensionRequestInfoUpdater.OnFailedSendingOrderExtensionRequest(shouldDismiss)
        }
    }

    private suspend fun onOrderExtensionRequestCompleted() {
        withContext(dispatcher.main) {
            orderExtensionRequestInfoUpdates.value =
                OrderExtensionRequestInfoUpdater.OnSuccessSendingOrderExtensionRequest()
        }
    }

    fun getSomRequestExtensionInfo(orderId: String) {
        launchCatchError(block = {
            val result = somGetOrderExtensionRequestInfoUseCase.execute(orderId, userSession.shopId, userSession.userId)
            _requestExtensionInfo.postValue(
                Success(
                    somGetOrderExtensionRequestInfoMapper.mapSuccessResponseToUiModel(
                        result
                    )
                )
            )
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
                    val mappedResult =
                        somOrderExtensionRequestResultMapper.mapResponseToUiModel(result)
                    _requestExtensionResult.postValue(Success(mappedResult))
                    if (!mappedResult.success) {
                        onFailedSendingOrderExtensionRequest(true)
                    } else {
                        onOrderExtensionRequestCompleted()
                    }
                } else {
                    onFailedSendingOrderExtensionRequest(false)
                }
            }, onError = {
                _requestExtensionResult.postValue(Fail(it))
                onFailedSendingOrderExtensionRequest(false)
            })
        }
    }

    fun updateOrderRequestExtensionInfoOnCommentChanged(element: OrderExtensionRequestInfoUiModel.CommentUiModel?) {
        element?.let {
            orderExtensionRequestInfoUpdates.value =
                OrderExtensionRequestInfoUpdater.OnCommentChange(it)
        }
    }

    fun updateOrderRequestExtensionInfoOnSelectedOptionChanged(element: OrderExtensionRequestInfoUiModel.OptionUiModel?) {
        element?.let {
            orderExtensionRequestInfoUpdates.value =
                OrderExtensionRequestInfoUpdater.OnSelectedOptionChange(it)
        }
    }
}