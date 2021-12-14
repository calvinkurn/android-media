package com.tokopedia.ordermanagement.buyercancellationorder.presentation.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel.BuyerNormalProductUiModel
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.model.BuyerCancelRequestReasonValidationResult
import com.tokopedia.ordermanagement.buyercancellationorder.common.utils.ResourceProvider
import com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason.BuyerGetCancellationReasonParam
import com.tokopedia.ordermanagement.buyercancellationorder.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.ordermanagement.buyercancellationorder.data.instantcancellation.BuyerInstantCancelParam
import com.tokopedia.ordermanagement.buyercancellationorder.data.requestcancel.BuyerRequestCancelData
import com.tokopedia.ordermanagement.buyercancellationorder.data.requestcancel.BuyerRequestCancelParam
import com.tokopedia.ordermanagement.buyercancellationorder.domain.BuyerGetCancellationReasonUseCase
import com.tokopedia.ordermanagement.buyercancellationorder.domain.BuyerInstantCancelUseCase
import com.tokopedia.ordermanagement.buyercancellationorder.domain.BuyerRequestCancelUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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
                                                     private val buyerRequestCancelUseCase: BuyerRequestCancelUseCase
) : BaseViewModel(dispatcher.main) {

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
    private val buyerRequestCancelReasonValidationRegex = Regex(
        ALLOWED_BUYER_REQUEST_CANCEL_REASON_INPUT
    )

    val buyerNormalProductUiModelListLiveData: LiveData<List<BuyerNormalProductUiModel>?> =
            Transformations.switchMap(_cancelReasonResult) { result ->
                getBundleUiModelFlow(result).asLiveData()
            }

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
                        _buyerRequestCancelReasonValidationResult.postValue(
                            BuyerCancelRequestReasonValidationResult(message, isError, isButtonEnable)
                        )
                    }
        }
    }

    private fun getBundleUiModelFlow(result: Result<BuyerGetCancellationReasonData.Data>): Flow<List<BuyerNormalProductUiModel>?> {
        return flow {
            val normalProductList =
                    if ((result as? Success)?.data?.getCancellationReason?.haveProductBundle == true) {
                        val bundlingProductList = result.data.getCancellationReason.bundleDetail?.bundleList?.flatMap {
                            it.orderDetailList.mapToNormalProductList()
                        }
                        bundlingProductList.orEmpty() + result.data.getCancellationReason.bundleDetail?.nonBundleList?.mapToNormalProductList().orEmpty()
                    } else {
                        null
                    }
            emit(normalProductList)
        }.flowOn(dispatcher.default)
    }

    private fun List<BuyerGetCancellationReasonData.Data.GetCancellationReason.OrderDetailsCancellation>.mapToNormalProductList(): List<BuyerNormalProductUiModel> {
        return map { bundleProduct ->
            BuyerNormalProductUiModel(
                    productId = bundleProduct.productId,
                    productThumbnailUrl = bundleProduct.picture,
                    productName = bundleProduct.productName,
                    productPrice = bundleProduct.productPrice
            )
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

}