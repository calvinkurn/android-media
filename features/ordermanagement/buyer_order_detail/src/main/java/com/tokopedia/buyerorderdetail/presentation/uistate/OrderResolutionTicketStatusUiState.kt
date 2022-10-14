package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUIModel

sealed interface OrderResolutionTicketStatusUiState {
    object Loading : OrderResolutionTicketStatusUiState

    sealed interface HasData : OrderResolutionTicketStatusUiState {
        val data: OrderResolutionUIModel

        data class Reloading(
            override val data: OrderResolutionUIModel
        ) : HasData

        data class Showing(
            override val data: OrderResolutionUIModel
        ) : HasData
    }

    object Hidden : OrderResolutionTicketStatusUiState
}
