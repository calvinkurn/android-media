package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.OrderInsuranceUiModel

sealed class OrderInsuranceUiState {
    object Loading : OrderInsuranceUiState()

    data class Showing(
        val data: OrderInsuranceUiModel
    ) : OrderInsuranceUiState()

    object Hidden : OrderInsuranceUiState()
}
