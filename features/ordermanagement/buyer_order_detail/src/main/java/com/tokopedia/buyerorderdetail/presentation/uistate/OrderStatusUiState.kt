package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel

sealed interface OrderStatusUiState {
    object Loading : OrderStatusUiState

    sealed interface HasData : OrderStatusUiState {
        val data: OrderStatusUiModel

        data class Reloading(
            override val data: OrderStatusUiModel
        ) : HasData

        data class Showing(
            override val data: OrderStatusUiModel
        ) : HasData
    }

    data class Error(
        val throwable: Throwable?
    ) : OrderStatusUiState
}
