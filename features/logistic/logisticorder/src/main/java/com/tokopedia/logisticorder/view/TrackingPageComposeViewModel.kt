package com.tokopedia.logisticorder.view

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticorder.mapper.TrackingPageMapperNew
import com.tokopedia.logisticorder.uimodel.TrackingPageEvent
import com.tokopedia.logisticorder.uimodel.TrackingPageState
import com.tokopedia.logisticorder.usecase.GetTrackingUseCase
import com.tokopedia.logisticorder.usecase.SetRetryAvailabilityUseCase
import com.tokopedia.logisticorder.usecase.SetRetryBookingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackingPageComposeViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val trackingUseCase: GetTrackingUseCase,
    private val setRetryBookingUseCase: SetRetryBookingUseCase,
    private val setRetryAvailabilityUseCase: SetRetryAvailabilityUseCase,
    private val mapper: TrackingPageMapperNew
) : BaseViewModel(dispatcher.main) {

    private var orderId: String = ""
    private var orderTxId: String? = null
    private var groupType: Int? = null
    private var trackingUrl: String? = null
    private var caller: String? = null
    private val _uiState = MutableStateFlow(TrackingPageState())
    val uiState: StateFlow<TrackingPageState> = _uiState.asStateFlow()

    fun onEvent(event: TrackingPageEvent) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        when (event) {
            is TrackingPageEvent.CheckAvailabilityToFindNewDriver -> {
                retryAvailability(event.orderId)
            }

            is TrackingPageEvent.FindNewDriver -> {
                retryBooking(event.orderId)
            }

            is TrackingPageEvent.LoadTrackingData -> {
                getTrackingData(
                    event.orderId,
                    event.orderTxId,
                    event.groupType,
                    event.userId,
                    event.deviceId
                )
            }
        }
    }

    private fun getTrackingData(
        orderId: String,
        orderTxId: String?,
        groupType: Int?,
        userId: String,
        deviceId: String
    ) {
        this.orderId = orderId
        viewModelScope.launch {
            try {
                val trackingParam = trackingUseCase.getParam(orderId, orderTxId, groupType, "")
                val getTrackingData = trackingUseCase(trackingParam)
                val uiModel = mapper.mapTrackingDataCompose(getTrackingData, userId, deviceId, orderId)
                _uiState.update {
                    it.copy(isLoading = false, trackingData = uiModel)
                }
            } catch (e: Throwable) {
                _uiState.update {
                    it.copy(isLoading = false, error = e)
                }
            }
        }
    }

    private fun retryBooking(orderId: String) {
        viewModelScope.launch {
            try {
                val retryBooking = setRetryBookingUseCase(orderId)
                _uiState.update {
                    it.copy(isLoading = false, retryBooking = retryBooking)
                }
            } catch (e: Throwable) {
                _uiState.update {
                    it.copy(isLoading = false, error = e)
                }
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
                _uiState.update {
                    it.copy(isLoading = false, error = e)
                }
            }
        }
    }
}
