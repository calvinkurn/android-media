package com.tokopedia.logisticorder.uimodel

import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse

sealed interface TrackingPageEvent {
    data class LoadTrackingData(val orderId: String, val orderTxId: String?, val groupType: Int?, val userId: String, val deviceId: String, val trackingUrl: String) : TrackingPageEvent
    data class FindNewDriver(val orderId: String) : TrackingPageEvent
    data class CheckAvailabilityToFindNewDriver(val orderId: String) : TrackingPageEvent
}

enum class ErrorTrackingPage {
    TRACKING_DATA, RETRY_BOOKING, RETRY_AVAILABILITY, DEFAULT
}

data class TrackingPageState(
    val isLoading: Boolean = false,
    val trackingData: TrackingDataModel? = null,
    val retryBooking: RetryBookingResponse? = null,
    val retryAvailability: RetryAvailabilityResponse? = null,
    val error: Throwable? = null
)
