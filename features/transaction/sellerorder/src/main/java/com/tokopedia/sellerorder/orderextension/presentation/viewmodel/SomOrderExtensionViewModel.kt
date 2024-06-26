package com.tokopedia.sellerorder.orderextension.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.orderextension.domain.usecases.GetOrderExtensionRequestInfoUseCase
import com.tokopedia.sellerorder.orderextension.domain.usecases.SendOrderExtensionRequestUseCase
import com.tokopedia.sellerorder.orderextension.presentation.mapper.GetOrderExtensionRequestInfoResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.mapper.OrderExtensionRequestResultResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUpdater
import com.tokopedia.user.session.UserSessionInterface
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
) : BaseViewModel(dispatcher.main) {
    private val _requestExtensionInfo = MutableLiveData<OrderExtensionRequestInfoUiModel>()
    val orderExtensionRequestInfo: LiveData<OrderExtensionRequestInfoUiModel>
        get() = _requestExtensionInfo

    private val orderExtensionRequestInfoUpdates = MutableLiveData<OrderExtensionRequestInfoUpdater>()

    init {
        launch {
            orderExtensionRequestInfoUpdates
                .asFlow()
                .collect { updateInfo ->
                    onNeedToUpdateOrderExtensionRequestInfo(updateInfo)
                }
        }
    }

    private suspend  fun onNeedToUpdateOrderExtensionRequestInfo(updateInfo: OrderExtensionRequestInfoUpdater) {
        _requestExtensionInfo.value?.let { oldRequestExtensionInfo ->
            withContext(dispatcher.computation) {
                updateInfo.execute(oldRequestExtensionInfo)
            }.also { newRequestExtensionInfo ->
                _requestExtensionInfo.value = newRequestExtensionInfo
            }
        }
    }

    private fun onLoadSomRequestExtensionInfo() {
        _requestExtensionInfo.value = somGetOrderExtensionRequestInfoMapper.createLoadingData()
    }

    private suspend fun onSuccessGetSomRequestExtensionInfo(mappedResult: OrderExtensionRequestInfoUiModel) =
        withContext(dispatcher.main) {
            orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
                .OnSuccessGetOrderExtensionRequest(mappedResult)
        }

    private suspend fun onFailedGetOrderExtensionRequest(throwable: Throwable) =
        withContext(dispatcher.main) {
            orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
                .OnFailedGetOrderExtensionRequest(throwable = throwable)
        }

    private fun startSendingOrderExtensionRequest(action: (OrderExtensionRequestInfoUiModel) -> Unit) {
        orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
            .OnStartSendingOrderExtensionRequest(action)
    }

    private suspend fun onFailedSendingOrderExtensionRequest(
        errorMessage: String,
        throwable: Throwable?,
        orderId: String,
        sendTracker: Boolean
    ) = withContext(dispatcher.main) {
        if (sendTracker) {
            SomAnalytics.eventFinishSendOrderExtensionRequest(
                shopId = userSession.shopId,
                orderId = orderId,
                success = false
            )
        }
        orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
            .OnFailedSendingOrderExtensionRequest(errorMessage, throwable)
    }

    private suspend fun onOrderExtensionRequestCompleted(message: String, orderId: String) =
        withContext(dispatcher.main) {
            SomAnalytics.eventFinishSendOrderExtensionRequest(
                shopId = userSession.shopId,
                orderId = orderId,
                success = true
            )
            orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
                .OnSuccessSendingOrderExtensionRequest(message)
        }

    fun getSomOrderExtensionRequestInfoLoadingState() {
        onLoadSomRequestExtensionInfo()
    }

    fun getSomOrderExtensionRequestInfo(orderId: String) {
        launchCatchError(context = dispatcher.io, block = {
            val result = somGetOrderExtensionRequestInfoUseCase.execute(orderId, userSession.shopId)
            val mappedResult = somGetOrderExtensionRequestInfoMapper
                .mapSuccessResponseToUiModel(result)
            onSuccessGetSomRequestExtensionInfo(mappedResult)
        }, onError = {
            onFailedGetOrderExtensionRequest(it)
        })
    }

    fun sendOrderExtensionRequest(orderId: String, extensionTime: Int) {
        startSendingOrderExtensionRequest { requestExtensionInfo ->
            launchCatchError(context = dispatcher.io, block = {
                if (requestExtensionInfo.isValid()) {
                    val selectedOptionCode = requestExtensionInfo.getSelectedOptionCode()
                    val result = somSendOrderRequestExtensionUseCase.execute(
                        orderId,
                        userSession.shopId,
                        selectedOptionCode,
                        requestExtensionInfo.getComment(selectedOptionCode),
                        extensionTime
                    )
                    val mappedResult = somOrderExtensionRequestResultMapper
                        .mapResponseToUiModel(result)
                    if (!mappedResult.success) {
                        onFailedSendingOrderExtensionRequest(
                            errorMessage = mappedResult.message,
                            orderId = orderId,
                            sendTracker = true,
                            throwable = null
                        )
                    } else {
                        onOrderExtensionRequestCompleted(
                            message = mappedResult.message,
                            orderId = orderId
                        )
                    }
                } else {
                    onFailedSendingOrderExtensionRequest(
                        errorMessage = "",
                        orderId = orderId,
                        sendTracker = false,
                        throwable = null
                    )
                }
            }, onError = {
                onFailedSendingOrderExtensionRequest(
                    errorMessage = "",
                    orderId = orderId,
                    sendTracker = true,
                    throwable = it
                )
            })
        }
    }

    fun updateOrderExtensionRequestInfoOnCommentChanged(element: OrderExtensionRequestInfoUiModel.CommentUiModel?) {
        element?.let {
            orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
                .OnCommentChange(it)
        }
    }

    fun updateOrderExtensionRequestInfoOnSelectedOptionChanged(element: OrderExtensionRequestInfoUiModel.OptionUiModel?) {
        element?.let {
            orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
                .OnSelectedOptionChange(it)
        }
    }

    fun requestDismissOrderExtensionRequestInfoBottomSheet() {
        orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
            .OnRequestDismissBottomSheet()
    }
}
