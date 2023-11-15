package com.tokopedia.buyerorderdetail.presentation.model

data class OrderOneTimeEventUiState(
    val event: OrderOneTimeEvent = OrderOneTimeEvent.Empty,
    val impressSavingsWidget: Boolean = false
)

sealed class OrderOneTimeEvent {
    data class ImpressSavingsWidget(
        val orderId: String,
        val isPlus: Boolean
    ) : OrderOneTimeEvent()

    object Empty : OrderOneTimeEvent()
}