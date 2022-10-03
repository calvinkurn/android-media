package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel

sealed class OrderStatusUiState {
    object Loading : OrderStatusUiState()

    data class Showing(
        val data: OrderStatusUiModel
    ) : OrderStatusUiState()

    data class Error(
        val throwable: Throwable
    ) : OrderStatusUiState()
}
