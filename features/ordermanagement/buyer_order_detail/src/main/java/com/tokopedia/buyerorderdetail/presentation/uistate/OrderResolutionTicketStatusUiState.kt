package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUiModel

sealed interface OrderResolutionTicketStatusUiState {
    object Loading : OrderResolutionTicketStatusUiState

    sealed interface HasData : OrderResolutionTicketStatusUiState {
        val data: OrderResolutionUiModel

        data class Reloading(
            override val data: OrderResolutionUiModel
        ) : HasData

        data class Showing(
            override val data: OrderResolutionUiModel
        ) : HasData

        object Hidden : HasData {
            override val data: OrderResolutionUiModel = OrderResolutionUiModel()
        }
    }
}
