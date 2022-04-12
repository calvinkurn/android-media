package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

data class OrderStatusLiveTrackingUiModel(
    val orderTrackingStatusInfoUiModel: OrderTrackingStatusInfoUiModel,
    val orderStatusKey: String,
    val estimationUiModel: OrderTrackingEstimationUiModel? = null,
    val invoiceOrderNumberUiModel: InvoiceOrderNumberUiModel? = null
)