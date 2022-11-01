package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

data class OrderStatusLiveTrackingUiModel(
    val tickerInfoData: TickerInfoData? = null,
    val orderTrackingStatusInfoUiModel: OrderTrackingStatusInfoUiModel,
    val orderStatusKey: String,
    val estimationUiModel: OrderTrackingEstimationUiModel? = null,
    val invoiceOrderNumberUiModel: InvoiceOrderNumberUiModel? = null,
    val toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel,
)
