package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUIModel

sealed class OrderResolutionTicketStatusUiState {
    object Loading : OrderResolutionTicketStatusUiState()
    data class Showing(
        val data: OrderResolutionUIModel
    ) : OrderResolutionTicketStatusUiState()
    object Hidden : OrderResolutionTicketStatusUiState()
}
