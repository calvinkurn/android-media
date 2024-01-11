package com.tokopedia.logisticorder.view

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticorder.mapper.TrackingPageMapperNew
import com.tokopedia.logisticorder.uimodel.TickerUnificationTargets
import com.tokopedia.logisticorder.uimodel.TrackingPageEvent
import com.tokopedia.logisticorder.uimodel.TrackingPageState
import com.tokopedia.logisticorder.usecase.GetTrackingUseCase
import com.tokopedia.logisticorder.usecase.SetRetryAvailabilityUseCase
import com.tokopedia.logisticorder.usecase.SetRetryBookingUseCase
import com.tokopedia.targetedticker.domain.GetTargetedTickerUseCase
import com.tokopedia.targetedticker.domain.TargetedTickerMapper
import com.tokopedia.targetedticker.domain.TargetedTickerPage
import com.tokopedia.targetedticker.domain.TargetedTickerParamModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackingPageComposeViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val trackingUseCase: GetTrackingUseCase,
    private val setRetryBookingUseCase: SetRetryBookingUseCase,
    private val setRetryAvailabilityUseCase: SetRetryAvailabilityUseCase,
    private val targetedTickerUseCase: GetTargetedTickerUseCase,
    private val userSession: UserSessionInterface,
    private val mapper: TrackingPageMapperNew
) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val DELAY_AFTER_RETRY_BOOKING_NEW_DRIVER = 5000L
        private const val SELLER_CALLER_KEY = "seller"
    }

    private var orderId: String = ""
    private var orderTxId: String? = null
    private var groupType: Int? = null
    private var trackingUrl: String? = null
    private var caller: String? = null
    private val _uiState = MutableStateFlow(TrackingPageState())
    val uiState: StateFlow<TrackingPageState> = _uiState.asStateFlow()
    private val _error = MutableSharedFlow<Throwable>(replay = 1)
    val error: SharedFlow<Throwable> = _error.asSharedFlow()

    fun onEvent(event: TrackingPageEvent) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        when (event) {
            is TrackingPageEvent.CheckAvailabilityToFindNewDriver -> {
                retryAvailability(orderId)
            }

            is TrackingPageEvent.FindNewDriver -> {
                retryBooking(orderId)
            }

            is TrackingPageEvent.LoadTrackingData -> {
                getTrackingData(
                    event.orderId,
                    event.orderTxId,
                    event.groupType,
                    event.trackingUrl,
                    event.pageCaller
                )
            }

            TrackingPageEvent.Refresh -> {
                getTrackingData(orderId, orderTxId, groupType, trackingUrl, caller.orEmpty())
            }
        }
    }

    private fun getTrackingData(
        orderId: String,
        orderTxId: String?,
        groupType: Int?,
        trackingUrlFromOrder: String?,
        pageCaller: String
    ) {
        this.orderId = orderId
        this.orderTxId = orderTxId
        this.groupType = groupType
        this.trackingUrl = trackingUrlFromOrder
        this.caller = pageCaller
        launchCatchError(
            block = {
                val trackingParam = trackingUseCase.getParam(orderId, orderTxId, groupType, "")
                val getTrackingData = trackingUseCase(trackingParam)
                val uiModel = mapper.mapTrackingDataCompose(
                    getTrackingData,
                    userSession.userId,
                    userSession.deviceId,
                    orderId,
                    trackingUrlFromOrder,
                    userSession.accessToken
                )
                _uiState.update {
                    it.copy(isLoading = false, trackingData = uiModel)
                }
                if ((!trackingUrl.isNullOrEmpty()) && caller.equals(
                        SELLER_CALLER_KEY,
                        ignoreCase = true
                    )
                ) {
                    retryAvailability(orderId)
                }
                getTickerData(uiModel.page.tickerUnificationTargets)
            },
            onError = { e ->
                _error.emit(e)
            }
        )
    }

    private fun getTickerData(tickerUnificationTargets: List<TickerUnificationTargets>) {
        launchCatchError(block = {
            val param = TargetedTickerParamModel(
                page = TargetedTickerPage.TRACKING_PAGE,
                targets = tickerUnificationTargets.map {
                    TargetedTickerParamModel.Target(it.type, it.values)
                }
            )
            val response = targetedTickerUseCase(param)
            val model =
                TargetedTickerMapper.convertTargetedTickerToUiModel(response.getTargetedTickerData)
            _uiState.update {
                it.copy(tickerData = model)
            }
        }, onError = { _error.emit(it) })
    }

    private fun retryBooking(orderId: String) {
        viewModelScope.launch {
            try {
                setRetryBookingUseCase(orderId)
                delay(DELAY_AFTER_RETRY_BOOKING_NEW_DRIVER)
                getTrackingData(orderId, orderTxId, groupType, trackingUrl, caller.orEmpty())
            } catch (e: Throwable) {
                _error.emit(e)
            }
        }
    }

    private fun retryAvailability(orderId: String) {
        viewModelScope.launch {
            try {
                val retryAvailability = setRetryAvailabilityUseCase(orderId)
                _uiState.update {
                    it.copy(isLoading = false, retryAvailability = retryAvailability)
                }
            } catch (e: Throwable) {
                _error.emit(e)
            }
        }
    }
}
