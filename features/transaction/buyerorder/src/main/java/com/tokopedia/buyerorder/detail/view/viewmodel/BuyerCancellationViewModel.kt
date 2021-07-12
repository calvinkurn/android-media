package com.tokopedia.buyerorder.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorder.common.ResourceProvider
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonParam
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelParam
import com.tokopedia.buyerorder.detail.data.requestcancel.BuyerRequestCancelData
import com.tokopedia.buyerorder.detail.data.requestcancel.BuyerRequestCancelParam
import com.tokopedia.buyerorder.detail.domain.BuyerGetCancellationReasonUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerInstantCancelUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerProductBundlingUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerRequestCancelUseCase
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerBundlingProductUiModel
import com.tokopedia.buyerorder.detail.view.model.BuyerCancelRequestReasonValidationResult
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/06/20.
 */
class BuyerCancellationViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                     private val resourceProvider: ResourceProvider,
                                                     private val getCancellationReasonUseCase: BuyerGetCancellationReasonUseCase,
                                                     private val buyerInstantCancelUseCase: BuyerInstantCancelUseCase,
                                                     private val buyerRequestCancelUseCase: BuyerRequestCancelUseCase,
                                                     private val buyerProductBundlingUseCase: BuyerProductBundlingUseCase) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val ALLOWED_BUYER_REQUEST_CANCEL_REASON_INPUT = "^[,.?!\\n A-Za-z0-9]+\$"
        private const val BUYER_REQUEST_CANCEL_REASON_MINIMAL_CHARACTER = 15
    }

    private val _cancelReasonResult = MutableLiveData<Result<BuyerGetCancellationReasonData.Data>>()
    val cancelReasonResult: LiveData<Result<BuyerGetCancellationReasonData.Data>>
        get() = _cancelReasonResult

    private val _buyerInstantCancelResult = MutableLiveData<Result<BuyerInstantCancelData.Data>>()
    val buyerInstantCancelResult: LiveData<Result<BuyerInstantCancelData.Data>>
        get() = _buyerInstantCancelResult

    private val _requestCancelResult = MutableLiveData<Result<BuyerRequestCancelData.Data>>()
    val requestCancelResult: LiveData<Result<BuyerRequestCancelData.Data>>
        get() = _requestCancelResult

    private val _buyerRequestCancelReasonValidationResult = MutableLiveData<BuyerCancelRequestReasonValidationResult>()
    val buyerRequestCancelReasonValidationResult: LiveData<BuyerCancelRequestReasonValidationResult>
        get() = _buyerRequestCancelReasonValidationResult

    private val buyerRequestCancelReasonValidation: MutableLiveData<String> = MutableLiveData()
    private val buyerRequestCancelReasonValidationRegex = Regex(ALLOWED_BUYER_REQUEST_CANCEL_REASON_INPUT)

    private val _buyerOrderId: MutableStateFlow<String?> = MutableStateFlow(null)
    @ExperimentalCoroutinesApi
    val buyerBundlingProductUiModelListLiveData: LiveData<List<BuyerBundlingProductUiModel>?> = _buyerOrderId.flatMapLatest {
        setProductListFlow(it)
    }.asLiveData()

    init {
        initBuyerRequestCancelReasonValidation()
    }

    private fun initBuyerRequestCancelReasonValidation() {
        launch {
            buyerRequestCancelReasonValidation
                    .asFlow()
                    .flowOn(dispatcher.computation)
                    .collectLatest {
                        val message: String
                        var isError = false
                        var isButtonEnable = false
                        if (it.isBlank()) {
                            message = resourceProvider.getBuyerRequestCancelReasonShouldNotContainsSpecialCharsErrorMessage()
                        } else if (!buyerRequestCancelReasonValidationRegex.matches(it)) {
                            message = resourceProvider.getBuyerRequestCancelReasonShouldNotContainsSpecialCharsErrorMessage()
                            isError = true
                        } else if (it.length < BUYER_REQUEST_CANCEL_REASON_MINIMAL_CHARACTER) {
                            message = resourceProvider.getBuyerRequestCancelReasonMinCharMessage()
                            isError = true
                        } else {
                            message = resourceProvider.getBuyerRequestCancelReasonShouldNotContainsSpecialCharsErrorMessage()
                            isButtonEnable = true
                        }
                        _buyerRequestCancelReasonValidationResult.postValue(BuyerCancelRequestReasonValidationResult(message, isError, isButtonEnable))
                    }
        }
    }

    fun getCancelReasons(cancelReasonQuery: String, userId: String, orderId: String) {
        launch {
            _cancelReasonResult.postValue(getCancellationReasonUseCase.execute(cancelReasonQuery, BuyerGetCancellationReasonParam(userId = userId, orderId = orderId)))
        }
    }

    fun instantCancellation(instantCancelQuery: String, orderId: String, reasonCode: String, reasonStr: String) {
        launch {
            _buyerInstantCancelResult.postValue(buyerInstantCancelUseCase.execute(instantCancelQuery, BuyerInstantCancelParam(orderId = orderId, reasonCode = reasonCode, reason = reasonStr)))
        }
    }

    fun requestCancel(requestCancelQuery: String, userId: String, orderId: String, reasonCode: String, reasonStr: String) {
        launch {
            _requestCancelResult.postValue(buyerRequestCancelUseCase.execute(requestCancelQuery, BuyerRequestCancelParam(userId = userId, orderId = orderId, reasonCode = reasonCode, reason = reasonStr)))
        }
    }

    fun validateBuyerRequestCancelReason(reason: String) {
        buyerRequestCancelReasonValidation.value = reason
    }

    fun getProductBundlingList(orderId: String?) {
        _buyerOrderId.value = orderId
    }

    private fun setProductListFlow(orderId: String?): Flow<List<BuyerBundlingProductUiModel>?> =
            flow {
                if (orderId == null) {
                    emit(null)
                } else {
                    emit(getCancellationProductList(orderId))
                }
            }.flowOn(dispatcher.io)

    private suspend fun getCancellationProductList(orderId: String): List<BuyerBundlingProductUiModel>? {
        buyerProductBundlingUseCase.execute(orderId).let { result ->
            return when(result) {
                is Success -> result.data
                else -> null
            }
        }
    }
}