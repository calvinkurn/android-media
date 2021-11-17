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
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
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
    val requestExtensionInfo: LiveData<OrderExtensionRequestInfoUiModel>
        get() = _requestExtensionInfo

    private val _requestExtensionResult = MutableLiveData<Result<OrderExtensionRequestResultUiModel>>()
    val requestExtensionResult: LiveData<Result<OrderExtensionRequestResultUiModel>>
        get() = _requestExtensionResult

    private val orderExtensionRequestInfoUpdates: MutableLiveData<OrderExtensionRequestInfoUpdater> = MutableLiveData()

    init {
        launch {
            orderExtensionRequestInfoUpdates
                .asFlow()
                .collect { updateInfo ->
                    onNeedToUpdateOrderExtensionRequestInfo(updateInfo)
                }
        }
    }

    private suspend fun onNeedToUpdateOrderExtensionRequestInfo(updateInfo: OrderExtensionRequestInfoUpdater) {
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

    private fun onSuccessGetSomRequestExtensionInfo(mappedResult: OrderExtensionRequestInfoUiModel) {
        orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
            .OnSuccessGetOrderExtensionRequest(mappedResult)
    }

    private fun onFailedGetOrderExtensionRequest(errorMessage: String) {
        orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
            .OnFailedGetOrderExtensionRequest(errorMessage)
    }

    private fun startSendingOrderExtensionRequest(action: (OrderExtensionRequestInfoUiModel) -> Unit) {
        orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
            .OnStartSendingOrderExtensionRequest(action)
    }

    private fun onFailedSendingOrderExtensionRequest(errorMessage: String) {
        orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
            .OnFailedSendingOrderExtensionRequest(errorMessage)
    }

    private fun onOrderExtensionRequestCompleted() {
        orderExtensionRequestInfoUpdates.value = OrderExtensionRequestInfoUpdater
            .OnSuccessSendingOrderExtensionRequest()
    }

    fun getSomRequestExtensionInfoLoadingState() {
        onLoadSomRequestExtensionInfo()
    }

    fun getSomRequestExtensionInfo(orderId: String) {
        onLoadSomRequestExtensionInfo()
        launchCatchError(block = {
            val result = somGetOrderExtensionRequestInfoUseCase.execute(orderId, userSession.shopId)
            val mappedResult = somGetOrderExtensionRequestInfoMapper.mapSuccessResponseToUiModel(result)
            onSuccessGetSomRequestExtensionInfo(mappedResult)
        }, onError = {
            val mappedError = somGetOrderExtensionRequestInfoMapper.mapError(it)
            onFailedGetOrderExtensionRequest(mappedError)
        })
    }

    fun sendOrderExtensionRequest(orderId: String) {
        startSendingOrderExtensionRequest { requestExtensionInfo ->
            launchCatchError(block = {
                if (requestExtensionInfo.isValid()) {
                    val selectedOptionCode = requestExtensionInfo.getSelectedOptionCode()
                    val result = somSendOrderRequestExtensionUseCase.execute(
                        orderId,
                        userSession.shopId,
                        selectedOptionCode,
                        requestExtensionInfo.getComment(selectedOptionCode)
                    )
                    val mappedResult = somOrderExtensionRequestResultMapper.mapResponseToUiModel(result)
                    if (!mappedResult.success) {
                        onFailedSendingOrderExtensionRequest(mappedResult.message)
                    } else {
                        onOrderExtensionRequestCompleted()
                    }
                } else {
                    onFailedSendingOrderExtensionRequest("")
                }
            }, onError = {
                val mappedError = somOrderExtensionRequestResultMapper.mapError(it)
                onFailedSendingOrderExtensionRequest(mappedError)
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

    fun requestDismissOrderExtensionRequestInfoBottomSheet() {
        orderExtensionRequestInfoUpdates.value =
            OrderExtensionRequestInfoUpdater.OnRequestDismissBottomSheet()
    }
}
