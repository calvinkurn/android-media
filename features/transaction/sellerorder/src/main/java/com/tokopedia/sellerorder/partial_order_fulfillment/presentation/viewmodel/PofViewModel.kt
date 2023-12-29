package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofEstimateRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofInfoRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestEstimateResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse.Data.InfoRequestPartialOrderFulfillment.Companion.STATUS_INITIAL
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.RequestState
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.SendPofRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.SendPofResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase.GetPofEstimateUseCase
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase.GetPofInfoUseCase
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase.SendPofUseCase
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductEditableUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofTickerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.mapper.PofToasterMapper
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.mapper.PofUiStateMapper
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.ToasterQueue
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEffect
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEvent
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiState
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.tracker.PofTracker
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class PofViewModel @Inject constructor(
    private val getPofInfoUseCase: GetPofInfoUseCase,
    private val getPofEstimateUseCase: GetPofEstimateUseCase,
    private val sendPofUseCase: SendPofUseCase,
    private val pofUiStateMapper: PofUiStateMapper,
    private val pofToasterMapper: PofToasterMapper,
    private val tracker: PofTracker
) : ViewModel() {

    companion object {
        private const val DELAY_FETCH_INITIAL_POF_INFO = 500L
        private const val DELAY_FETCH_POF_INFO_ON_ERROR = 1000L
        private const val DELAY_FETCH_INITIAL_POF_ESTIMATE = 0L
        private const val DELAY_FETCH_POF_ESTIMATE_ON_PRODUCT_QUANTITY_CHANGED = 1000L

        private const val SAVED_STATE_KEY_ORDER_ID = "orderId"
        private const val SAVED_STATE_KEY_INITIAL_POF_STATUS = "initialPofStatus"
        private const val SAVED_STATE_KEY_QUANTITY_EDITOR_DATA_LIST = "quantityEditorDataList"
    }

    private var getPofInfoJob: Job? = null
    private var getPofEstimateJob: Job? = null
    private var sendPofJob: Job? = null

    private var orderId = 0L
    private var initialPofStatus = STATUS_INITIAL
    private var quantityEditorDataList = listOf<PofProductEditableUiModel.QuantityEditorData>()
    private var detailInfo = listOf<GetPofEstimateRequestParams.DetailInfo>()
    private var showBottomSheetSummary = false
    private var tickerUiModel: PofTickerUiModel? = null

    private var getPofInfoRequestState: RequestState<GetPofRequestInfoResponse.Data> = RequestState.None
    private var getPofEstimateRequestState: RequestState<GetPofRequestEstimateResponse.Data> = RequestState.None
    private var sendPofRequestState: RequestState<SendPofResponse.Data> = RequestState.None

    private val _uiEvent: MutableSharedFlow<UiEvent> = MutableSharedFlow()

    private val _uiEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _toasterQueue: MutableSharedFlow<ToasterQueue> = MutableSharedFlow()
    val toasterQueue: SharedFlow<ToasterQueue> = _toasterQueue.asSharedFlow()

    init {
        startUiEventCollector()
    }

    fun onEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.emit(event) }
    }

    private fun startUiEventCollector() {
        viewModelScope.launch {
            _uiEvent.collect { event ->
                when (event) {
                    is UiEvent.ClickRetryOnErrorState -> onClickRetryOnErrorState()
                    is UiEvent.OnClickDescriptionLearnMore -> onClickDescriptionLearnMore()
                    is UiEvent.OnClickDismissPofBottomSheet -> onClickDismissPofBottomSheet()
                    is UiEvent.OnClickDismissSummaryBottomSheet -> onClickDismissSummaryBottomSheet()
                    is UiEvent.OnClickOpenPofInfoSummary -> onClickOpenPofInfoSummary()
                    is UiEvent.OnClickResetPofForm -> onClickResetPofForm()
                    is UiEvent.OnClickRetryFetchPofEstimate -> onClickRetryFetchPofEstimate()
                    is UiEvent.OnClickSendPof -> onClickSendPof(event)
                    is UiEvent.OnTryChangeProductQuantityAboveMaxQuantity -> onTryChangeProductQuantityAboveMaxQuantity()
                    is UiEvent.OnTryChangeProductQuantityBelowMinQuantity -> onTryChangeProductQuantityBelowMinQuantity()
                    is UiEvent.OpenScreen -> onOpenScreen(event)
                    is UiEvent.ProductQuantityChanged -> onProductQuantityChanged(event)
                    is UiEvent.RestoreState -> onRestoreState(event)
                    is UiEvent.SaveState -> onSaveState(event)
                    is UiEvent.None -> { /* noop */ }
                }
            }
        }
    }

    private fun onClickRetryOnErrorState() {
        getPofInfo(DELAY_FETCH_POF_INFO_ON_ERROR)
    }

    private fun onClickDescriptionLearnMore() {
        tracker.trackClickDescriptionLearnMore()
    }

    private fun onClickDismissPofBottomSheet() {
        viewModelScope.launch {
            val payload = if (sendPofRequestState is RequestState.Error) {
                UiEffect.FinishActivity(Activity.RESULT_OK)
            } else {
                UiEffect.FinishActivity(Activity.RESULT_CANCELED)
            }
            _uiEffect.emit(payload)
        }
    }

    private fun onClickDismissSummaryBottomSheet() {
        showBottomSheetSummary = false
        updateUiState()
    }

    private fun onClickOpenPofInfoSummary() {
        tracker.trackClickOpenPofSummaryBottomSheet()
        showBottomSheetSummary = true
        updateUiState()
    }

    private fun onClickResetPofForm() {
        getPofInfoRequestState.let { requestState ->
            if (requestState is RequestState.Success) {
                tracker.trackClickReset()
                updateQuantityEditorDataList(requestState.data)
                updateDetailInfo()
                updateUiState()
                getPofEstimate(DELAY_FETCH_INITIAL_POF_ESTIMATE)
            }
        }
    }

    private fun onClickRetryFetchPofEstimate() {
        getPofEstimate(DELAY_FETCH_INITIAL_POF_ESTIMATE)
    }

    private fun onClickSendPof(event: UiEvent.OnClickSendPof) {
        tracker.trackClickSend()
        sendPof(event.pofDetailList)
    }

    private fun onTryChangeProductQuantityAboveMaxQuantity() {
        showToasterCannotExceedCheckoutQuantity()
    }

    private fun onTryChangeProductQuantityBelowMinQuantity() {
        showToasterCannotEmptyAllProduct()
    }

    private fun onOpenScreen(event: UiEvent.OpenScreen) {
        orderId = event.orderId
        initialPofStatus = event.initialPofStatus
        getPofInfo(DELAY_FETCH_INITIAL_POF_INFO)
    }

    private fun onProductQuantityChanged(event: UiEvent.ProductQuantityChanged) {
        trackProductQuantityChange(event)
        updateQuantityEditorDataList(event.orderDetailId, event.newQuantity)
        updateDetailInfo()
        getPofEstimate(DELAY_FETCH_POF_ESTIMATE_ON_PRODUCT_QUANTITY_CHANGED)
        updateUiState()
    }

    private fun onRestoreState(event: UiEvent.RestoreState) {
        val savedOrderId = event.bundle.getLong(SAVED_STATE_KEY_ORDER_ID, -1)
        val savedInitialPofStatus = event.bundle.getInt(SAVED_STATE_KEY_INITIAL_POF_STATUS, -1)
        val savedQuantityEditorDataList = event.bundle.getParcelableArrayList<PofProductEditableUiModel.QuantityEditorData>(SAVED_STATE_KEY_QUANTITY_EDITOR_DATA_LIST).orEmpty()
        if (savedOrderId == -1L || savedInitialPofStatus == -1 || savedQuantityEditorDataList.isEmpty()) {
            event.onFailedRestoreState()
        } else {
            orderId = savedOrderId
            initialPofStatus = savedInitialPofStatus
            quantityEditorDataList = savedQuantityEditorDataList.toList()
            updateDetailInfo()
            getPofInfo(DELAY_FETCH_INITIAL_POF_INFO, CacheType.CACHE_FIRST)
        }
    }

    private fun onSaveState(event: UiEvent.SaveState) {
        event.bundle.putLong(SAVED_STATE_KEY_ORDER_ID, orderId)
        event.bundle.putInt(SAVED_STATE_KEY_INITIAL_POF_STATUS, initialPofStatus)
        event.bundle.putParcelableArrayList(SAVED_STATE_KEY_QUANTITY_EDITOR_DATA_LIST, ArrayList(quantityEditorDataList))
    }

    private fun showToasterCannotEmptyAllProduct() {
        viewModelScope.launch {
            _toasterQueue.emit(pofToasterMapper.mapToasterCannotEmptyAllProduct())
        }
    }

    private fun showToasterCannotExceedCheckoutQuantity() {
        viewModelScope.launch {
            _toasterQueue.emit(pofToasterMapper.mapToasterCannotExceedCheckoutQuantity())
        }
    }

    private fun showToasterErrorSendPof() {
        viewModelScope.launch {
            _toasterQueue.emit(pofToasterMapper.mapToasterErrorSendPof())
        }
    }

    private fun showToasterErrorReFetchPofEstimate() {
        viewModelScope.launch {
            _toasterQueue.emit(pofToasterMapper.mapToasterErrorReFetchPofEstimate())
        }
    }

    private fun getPofInfo(delay: Long, cacheType: CacheType = CacheType.ALWAYS_CLOUD) {
        getPofInfoJob?.cancel()
        getPofInfoJob = viewModelScope.launchCatchError(block = {
            getPofInfoUseCase(
                GetPofInfoRequestParams(
                    orderId = orderId,
                    delay = delay,
                    cacheType = cacheType
                )
            ).collectLatest { requestState ->
                onGetPofInfoRequestStateChanged(requestState, cacheType)
            }
        }, onError = {
            onGetPofInfoRequestStateChanged(RequestState.Error(it), cacheType)
        })
    }

    private fun getPofEstimate(delay: Long, cacheType: CacheType = CacheType.ALWAYS_CLOUD) {
        getPofEstimateJob?.cancel()
        getPofEstimateJob = viewModelScope.launchCatchError(block = {
            getPofEstimateUseCase(
                GetPofEstimateRequestParams(
                    orderId = orderId,
                    detailInfo = detailInfo,
                    delay = delay,
                    cacheType = cacheType
                )
            ).collectLatest { requestState ->
                onGetPofEstimateRequestStateChanged(requestState)
            }
        }, onError = {
            onGetPofEstimateRequestStateChanged(RequestState.Error(it))
        })
    }

    private fun sendPof(pofDetailList: List<SendPofRequestParams.PofDetail>) {
        sendPofJob?.cancel()
        sendPofJob = viewModelScope.launchCatchError(block = {
            sendPofUseCase(
                SendPofRequestParams(
                    orderId = orderId,
                    pofDetail = pofDetailList
                )
            ).collectLatest { requestState ->
                onSendPofRequestStateChanged(requestState)
            }
        }, onError = {
            onSendPofRequestStateChanged(RequestState.Error(it))
        })
    }

    private fun updateUiState() {
        _uiState.value = pofUiStateMapper.map(
            pofInfoRequestState = getPofInfoRequestState,
            pofEstimateRequestState = getPofEstimateRequestState,
            sendPofRequestState = sendPofRequestState,
            tickerUiModel = tickerUiModel,
            quantityEditorDataList = quantityEditorDataList,
            showBottomSheetSummary = showBottomSheetSummary,
            initialPofStatus = initialPofStatus
        )
    }

    private fun updateTicker() {
        tickerUiModel = getPofEstimateRequestState.let { requestState ->
            if (requestState is RequestState.Success) {
                val tickerData = requestState.data.partialOrderFulfillmentRequestEstimate?.pofInfo
                if (tickerData?.hasValidInfo() == true) {
                    PofTickerUiModel(text = tickerData.text.orEmpty())
                } else tickerUiModel
            } else tickerUiModel
        }
    }

    private fun onGetPofInfoRequestStateChanged(
        requestState: RequestState<GetPofRequestInfoResponse.Data>,
        cacheType: CacheType
    ) {
        getPofInfoRequestState = requestState
        when (requestState) {
            is RequestState.Error -> {
                logError(requestState.throwable)
                getPofEstimateRequestState = RequestState.Error(requestState.throwable)
            }
            is RequestState.Success -> {
                if (cacheType == CacheType.ALWAYS_CLOUD) {
                    updateQuantityEditorDataList(requestState.data)
                }
                updateDetailInfo()
                getPofEstimate(DELAY_FETCH_INITIAL_POF_ESTIMATE, cacheType)
            }
            is RequestState.Requesting -> getPofEstimateRequestState = RequestState.Requesting
            else -> { /* noop */ }
        }
        updateUiState()
    }

    private fun onGetPofEstimateRequestStateChanged(
        requestState: RequestState<GetPofRequestEstimateResponse.Data>
    ) {
        getPofEstimateRequestState = requestState
        shouldShowErrorReFetchEstimateToaster()
        updateTicker()
        updateUiState()
    }

    private fun onSendPofRequestStateChanged(requestState: RequestState<SendPofResponse.Data>) {
        sendPofRequestState = requestState
        when (requestState) {
            is RequestState.Error -> {
                logError(requestState.throwable)
                showToasterErrorSendPof()
            }
            is RequestState.Success -> {
                viewModelScope.launch { _uiEffect.emit(UiEffect.FinishActivity(Activity.RESULT_OK)) }
            }
            else -> { /* noop */ }
        }
        updateUiState()
    }

    private fun shouldShowErrorReFetchEstimateToaster() {
        val reFetchEstimateRequestState = getPofEstimateRequestState
        if (reFetchEstimateRequestState is RequestState.Error) {
            logError(reFetchEstimateRequestState.throwable)
            val isErrorReFetchPofEstimate = getPofInfoRequestState is RequestState.Success
            if (isErrorReFetchPofEstimate) {
                showToasterErrorReFetchPofEstimate()
            }
        }
    }

    private fun updateQuantityEditorDataList(data: GetPofRequestInfoResponse.Data) {
        quantityEditorDataList = pofUiStateMapper.mapInitialQuantityEditorData(data)
    }

    private fun updateQuantityEditorDataList(orderDetailId: Long, quantity: Int) {
        val nonZeroProductQuantityCount = quantityEditorDataList.count {
            it.orderDetailId != orderDetailId && it.quantity > Int.ZERO
        }
        val minQuantity = if (quantity == Int.ZERO) {
            if (nonZeroProductQuantityCount > Int.ONE) Int.ZERO else Int.ONE
        } else {
            if (nonZeroProductQuantityCount > Int.ZERO) Int.ZERO else Int.ONE
        }
        quantityEditorDataList = quantityEditorDataList.map { quantityEditorData ->
            if (quantityEditorData.orderDetailId == orderDetailId) {
                quantityEditorData.copy(
                    quantity = quantity,
                    minQuantity = minQuantity.coerceAtMost(quantity),
                    updateTimestamp = System.currentTimeMillis()
                )
            } else {
                if (quantityEditorData.quantity != Int.ZERO) {
                    quantityEditorData.copy(minQuantity = minQuantity)
                } else {
                    quantityEditorData
                }
            }
        }
    }

    private fun updateDetailInfo() {
        detailInfo = quantityEditorDataList.map { quantityEditorData ->
            GetPofEstimateRequestParams.DetailInfo(
                orderDetailId = quantityEditorData.orderDetailId,
                productId = quantityEditorData.productId,
                quantityRequest = quantityEditorData.quantity
            )
        }
    }

    private fun logError(throwable: Throwable) {
        ErrorHandler.getErrorMessagePair(null, throwable, ErrorHandler.Builder().build())
    }

    private fun trackProductQuantityChange(event: UiEvent.ProductQuantityChanged) {
        if (event.newQuantity < event.oldQuantity) {
            tracker.trackClickReduceQuantity()
        } else {
            tracker.trackClickIncreaseQuantity()
        }
    }
}
