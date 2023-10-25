package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.extension.combine
import com.tokopedia.sellerorder.orderextension.presentation.model.StringRes
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofEstimateRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofInfoRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestEstimateResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse.Data.InfoRequestPartialOrderFulfillment.Companion.STATUS_INITIAL
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse.Data.InfoRequestPartialOrderFulfillment.Companion.STATUS_INVALID
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.RequestState
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.SendPofRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.SendPofResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase.GetPofEstimateUseCase
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase.GetPofInfoUseCase
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase.SendPofUseCase
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductEditableUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofTickerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.mapper.PofUiStateMapper
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.ToasterQueue
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEffect
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEvent
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiState
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class PofViewModel @Inject constructor(
    private val getPofInfoUseCase: GetPofInfoUseCase,
    private val getPofEstimateUseCase: GetPofEstimateUseCase,
    private val sendPofUseCase: SendPofUseCase,
    private val pofUiStateMapper: PofUiStateMapper
) : ViewModel() {

    companion object {
        private const val STATE_FLOW_SHARING_DURATION = 5000L
        private const val DELAY_FETCH_INITIAL_POF_INFO = 0L
        private const val DELAY_FETCH_POF_INFO_ON_ERROR = 1000L
        private const val DELAY_FETCH_INITIAL_POF_ESTIMATE = 0L
        private const val DELAY_FETCH_POF_ESTIMATE_ON_PRODUCT_QUANTITY_CHANGED = 1000L
    }

    private val _uiEvent: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    private val _uiEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    private val fetchPofInfo = MutableSharedFlow<Unit>(replay = Int.ONE)
    private val fetchPofEstimate = MutableSharedFlow<Unit>(replay = Int.ONE)
    private val sendPof = MutableSharedFlow<Unit>(replay = Int.ONE)
    private val delayFetchPofInfo = MutableStateFlow(DELAY_FETCH_INITIAL_POF_INFO)
    private val delayFetchPofEstimate = MutableStateFlow(DELAY_FETCH_INITIAL_POF_ESTIMATE)
    private val orderId = MutableStateFlow(Long.ZERO)
    private val initialPofStatus = MutableStateFlow(STATUS_INITIAL)
    private val quantityEditorDataList = MutableStateFlow<List<PofProductEditableUiModel.QuantityEditorData>>(listOf())
    private val showBottomSheetSummary = MutableStateFlow(false)
    private val pofDetailList = MutableStateFlow(listOf<SendPofRequestParams.PofDetail>())

    private val detailInfo = mapDetailInfo()

    private val getPofInfoRequestParams = mapGetPofInfoRequestParam()
    private val getPofEstimateRequestParams = mapGetPofEstimateRequestParam()
    private val sendPofRequestParams = mapSendPofRequestParams()

    private val getPofInfoRequestState = mapGetPofInfoRequestState()
    private val getPofEstimateRequestState = mapGetPofEstimateRequestState()
    private val sendPofRequestState = mapSendPofRequestState()

    private val tickerUiModel = mapTickerUiModel()

    val uiState: StateFlow<UiState> = mapUiState()

    private val _toasterQueue: MutableSharedFlow<ToasterQueue> = MutableSharedFlow()
    val toasterQueue: SharedFlow<ToasterQueue> = _toasterQueue.asSharedFlow()

    init {
        startUiEventCollector()
        mapInitialQuantityEditorDataList()
        mapToasterErrorSendPof()
        mapToasterErrorReFetchPofEstimate()
        mapCloseBottomSheet()
    }

    fun onEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.emit(event) }
    }

    private fun mapDetailInfo(): StateFlow<List<GetPofEstimateRequestParams.DetailInfo>> {
        return quantityEditorDataList.mapLatest { quantityEditorDataList ->
            quantityEditorDataList.map { quantityEditorData ->
                GetPofEstimateRequestParams.DetailInfo(
                    orderDetailId = quantityEditorData.orderDetailId,
                    productId = quantityEditorData.productId,
                    quantityRequest = quantityEditorData.quantity
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_SHARING_DURATION),
            initialValue = listOf()
        )
    }

    private fun mapGetPofInfoRequestParam(): StateFlow<GetPofInfoRequestParams> {
        return combine(orderId, delayFetchPofInfo) { orderId, delay ->
            GetPofInfoRequestParams(orderId = orderId, delay = delay)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_SHARING_DURATION),
            initialValue = GetPofInfoRequestParams()
        )
    }

    private fun mapGetPofEstimateRequestParam(): StateFlow<GetPofEstimateRequestParams> {
        return combine(
            orderId,
            detailInfo,
            delayFetchPofEstimate
        ) { orderId, detailInfo, delay ->
            GetPofEstimateRequestParams(
                detailInfo = detailInfo,
                orderId = orderId,
                delay = delay
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_SHARING_DURATION),
            initialValue = GetPofEstimateRequestParams()
        )
    }

    private fun mapSendPofRequestParams(): StateFlow<SendPofRequestParams> {
        return combine(
            orderId,
            pofDetailList
        ) { orderId, pofDetailList ->
            SendPofRequestParams(
                orderId = orderId,
                pofDetail = pofDetailList
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_SHARING_DURATION),
            initialValue = SendPofRequestParams()
        )
    }

    private fun mapGetPofInfoRequestState(): StateFlow<RequestState<GetPofRequestInfoResponse.Data>> {
        return combine(
            getPofInfoRequestParams,
            fetchPofInfo
        ) { pofRequestInfoRequestParams, _ ->
            pofRequestInfoRequestParams
        }.flatMapLatest { requestParams ->
            getPofInfoUseCase(requestParams)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_SHARING_DURATION),
            initialValue = RequestState.None
        )
    }

    private fun mapGetPofEstimateRequestState(): StateFlow<RequestState<GetPofRequestEstimateResponse.Data>> {
        return combine(
            getPofEstimateRequestParams,
            getPofInfoRequestState,
            fetchPofEstimate
        ) { pofRequestEstimateRequestParams, getPofRequestInfoRequestState, _ ->
            Pair(pofRequestEstimateRequestParams, getPofRequestInfoRequestState)
        }.flatMapLatest { (params, getPofRequestInfoRequestState) ->
            if (getPofRequestInfoRequestState is RequestState.Error) {
                flowOf(RequestState.Error(getPofRequestInfoRequestState.throwable))
            } else {
                getPofEstimateUseCase(params)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_SHARING_DURATION),
            initialValue = RequestState.None
        )
    }

    private fun mapSendPofRequestState(): StateFlow<RequestState<SendPofResponse.Data>> {
        return combine(
            sendPofRequestParams,
            sendPof
        ) { params, _ ->
            params
        }.flatMapLatest { params ->
            sendPofUseCase(params)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_SHARING_DURATION),
            initialValue = RequestState.None
        )
    }

    private fun mapTickerUiModel(): StateFlow<PofTickerUiModel?> {
        return getPofEstimateRequestState.mapLatest { pofRequestEstimateRequestState ->
            val currentModel = tickerUiModel.value
            if (pofRequestEstimateRequestState is RequestState.Success) {
                pofUiStateMapper.mapTicker(pofRequestEstimateRequestState.data.partialOrderFulfillmentRequestEstimate?.pofInfo)
            } else currentModel
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_SHARING_DURATION),
            initialValue = null
        )
    }

    private fun mapUiState(): StateFlow<UiState> {
        return combine(
            getPofInfoRequestState,
            getPofEstimateRequestState,
            sendPofRequestState,
            tickerUiModel,
            quantityEditorDataList,
            showBottomSheetSummary,
            initialPofStatus,
            pofUiStateMapper::map
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_SHARING_DURATION),
            initialValue = pofUiStateMapper.mapLoadingState(STATUS_INVALID)
        )
    }

    private fun startUiEventCollector() {
        viewModelScope.launch {
            _uiEvent.collect { event ->
                when (event) {
                    is UiEvent.ClickRetryOnErrorState -> onClickRetryOnErrorState()
                    is UiEvent.None -> { /* noop */ }
                    is UiEvent.OnClickDismissSummaryBottomSheet -> onClickDismissSummaryBottomSheet()
                    is UiEvent.OnClickOpenPofInfoSummary -> onClickOpenPofInfoSummary()
                    is UiEvent.OnClickRetryFetchPofEstimate -> onClickRetryFetchPofEstimate()
                    is UiEvent.OnClickSendPof -> onClickSendPof(event)
                    is UiEvent.OpenScreen -> onOpenScreen(event)
                    is UiEvent.ProductAvailableQuantityChanged -> onProductAvailableQuantityChanged(event)
                }
            }
        }
    }

    private fun mapInitialQuantityEditorDataList() {
        viewModelScope.launch {
            getPofInfoRequestState.collectLatest { pofRequestInfoRequestState ->
                if (pofRequestInfoRequestState is RequestState.Success && quantityEditorDataList.value.isEmpty()) {
                    val pofStatus = pofRequestInfoRequestState.data.infoRequestPartialOrderFulfillment?.pofStatus.orZero()
                    val details = if (pofStatus == STATUS_INITIAL) {
                        pofRequestInfoRequestState
                            .data
                            .infoRequestPartialOrderFulfillment
                            ?.detailsOriginal
                    } else {
                        pofRequestInfoRequestState
                            .data
                            .infoRequestPartialOrderFulfillment
                            ?.detailsFulfilled
                    }
                    quantityEditorDataList.value = details?.map { detail ->
                        PofProductEditableUiModel.QuantityEditorData(
                            orderDetailId = detail?.orderDetailId.orZero(),
                            productId = detail?.productId.orZero(),
                            quantity = detail?.quantityRequest.orZero(),
                            maxQuantity = detail?.quantityCheckout.orZero(),
                            updateTimestamp = Long.ZERO,
                            enabled = true
                        )
                    }.orEmpty()
                    delayFetchPofEstimate.emit(DELAY_FETCH_INITIAL_POF_ESTIMATE)
                }
            }
        }
    }

    private fun mapToasterErrorSendPof() {
        viewModelScope.launch {
            sendPofRequestState.collectLatest { requestState ->
                if (requestState is RequestState.Error) {
                    showToasterErrorSendPof()
                } else if (
                    requestState is RequestState.Success &&
                    requestState.data.requestPartialOrderFulfillment?.success != Int.ONE
                ) {
                    showToasterErrorSendPof()
                }
            }
        }
    }

    private fun mapToasterErrorReFetchPofEstimate() {
        viewModelScope.launch {
            getPofEstimateRequestState.collectLatest { requestState ->
                if (requestState is RequestState.Error) {
                    showToasterErrorReFetchPofEstimate()
                }
            }
        }
    }

    private fun mapCloseBottomSheet() {
        viewModelScope.launch {
            sendPofRequestState.collectLatest { requestState ->
                if (
                    requestState is RequestState.Success &&
                    requestState.data.requestPartialOrderFulfillment?.success == Int.ONE
                ) {
                    _uiEffect.emit(UiEffect.FinishActivity)
                }
            }
        }
    }

    private suspend fun onClickRetryOnErrorState() {
        delayFetchPofInfo.emit(DELAY_FETCH_POF_INFO_ON_ERROR)
        fetchPofInfo.emit(Unit)
        fetchPofEstimate.emit(Unit)
    }

    private suspend fun onClickDismissSummaryBottomSheet() {
        showBottomSheetSummary.emit(false)
    }

    private suspend fun onClickOpenPofInfoSummary() {
        showBottomSheetSummary.emit(true)
    }

    private suspend fun onClickRetryFetchPofEstimate() {
        delayFetchPofEstimate.emit(DELAY_FETCH_INITIAL_POF_ESTIMATE)
        fetchPofEstimate.emit(Unit)
    }

    private suspend fun onClickSendPof(event: UiEvent.OnClickSendPof) {
        pofDetailList.emit(event.pofDetailList)
        sendPof.emit(Unit)
    }

    private suspend fun onOpenScreen(event: UiEvent.OpenScreen) {
        orderId.emit(event.orderId)
        initialPofStatus.emit(event.initialPofStatus)
        fetchPofInfo.emit(Unit)
        fetchPofEstimate.emit(Unit)
    }

    private suspend fun onProductAvailableQuantityChanged(event: UiEvent.ProductAvailableQuantityChanged) {
        var changed = false
        quantityEditorDataList.update { quantityEditorDataList ->
            quantityEditorDataList.map { quantityEditorData ->
                if (quantityEditorData.orderDetailId == event.orderDetailId) {
                    if (event.exceedCheckoutQuantity) {
                        changed = quantityEditorData.quantity != event.availableQuantity
                        if (changed) {
                            quantityEditorData.copy(
                                quantity = event.availableQuantity,
                                updateTimestamp = System.currentTimeMillis()
                            )
                        } else {
                            showToasterCannotExceedCheckoutQuantity()
                            quantityEditorData.copy(updateTimestamp = System.currentTimeMillis())
                        }
                    } else {
                        val othersQuantityIsEmpty = quantityEditorDataList.none {
                            it.orderDetailId != event.orderDetailId && it.quantity.isMoreThanZero()
                        }
                        changed = !(othersQuantityIsEmpty && event.availableQuantity.isZero())
                        if (changed) {
                            quantityEditorData.copy(
                                quantity = event.availableQuantity,
                                updateTimestamp = System.currentTimeMillis()
                            )
                        } else {
                            showToasterCannotEmptyAllProduct()
                            quantityEditorData.copy(updateTimestamp = System.currentTimeMillis())
                        }
                    }
                } else quantityEditorData
            }
        }
        if (changed) delayFetchPofEstimate.emit(DELAY_FETCH_POF_ESTIMATE_ON_PRODUCT_QUANTITY_CHANGED)
    }

    private suspend fun showToasterCannotEmptyAllProduct() {
        _toasterQueue.emit(ToasterQueue(text = StringRes(R.string.som_pof_toaster_error_cannot_empty_all_products)))
    }

    private suspend fun showToasterCannotExceedCheckoutQuantity() {
        _toasterQueue.emit(ToasterQueue(text = StringRes(R.string.som_pof_toaster_error_cannot_exceed_checkout_quantity)))
    }

    private suspend fun showToasterErrorSendPof() {
        _toasterQueue.emit(
            ToasterQueue(
                text = StringRes(R.string.som_pof_toaster_error_send_pof),
                type = Toaster.TYPE_ERROR
            )
        )
    }

    private suspend fun showToasterErrorReFetchPofEstimate() {
        _toasterQueue.emit(
            ToasterQueue(
                text = StringRes(R.string.som_pof_toaster_error_re_fetch_pof_estimate),
                type = Toaster.TYPE_ERROR
            )
        )
    }
}
