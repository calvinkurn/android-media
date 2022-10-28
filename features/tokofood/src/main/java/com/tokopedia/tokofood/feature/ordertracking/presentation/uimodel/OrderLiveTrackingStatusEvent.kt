package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

sealed class OrderLiveTrackingStatusEvent {
    data class Success(val orderStatusLiveTrackingUiModel: OrderStatusLiveTrackingUiModel) : OrderLiveTrackingStatusEvent()
    data class Error(val error: Throwable) : OrderLiveTrackingStatusEvent()
}