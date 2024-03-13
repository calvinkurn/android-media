package com.tokopedia.buyerorderdetail.presentation.model

data class WidgetBrcCsatUiModel(
    val orderID: String,
    val helpUrl: String,
    val expanded: Boolean,
    val autoOpenCsatForm: Boolean
) {
    var autoOpenCompleted: Boolean = false
}
