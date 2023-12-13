package com.tokopedia.logisticorder.uimodel

import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse

sealed interface TrackingPageState {
    object FirstLoad : TrackingPageState
    data class SuccessGetTrackingData(val trackingData: TrackingDataModel) : TrackingPageState
    data class TrackingPageError(val type: ErrorTrackingPage, val error: Throwable) :
        TrackingPageState

    data class FoundNewDriver(val response: RetryBookingResponse) : TrackingPageState
    data class AvailabilityForNewDriver(val response: RetryAvailabilityResponse) : TrackingPageState
}

sealed interface TrackingPageEvent {
    data class LoadTrackingData(val orderId: String, val orderTxId: String?, val groupType: Int?) : TrackingPageEvent
    data class FindNewDriver(val orderId: String) : TrackingPageEvent
    data class CheckAvailabilityToFindNewDriver(val orderId: String) : TrackingPageEvent
}

enum class ErrorTrackingPage {
    TRACKING_DATA, RETRY_BOOKING, RETRY_AVAILABILITY, DEFAULT
}
