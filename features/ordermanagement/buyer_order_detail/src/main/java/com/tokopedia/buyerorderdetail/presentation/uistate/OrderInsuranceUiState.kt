package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.OrderInsuranceUiModel

sealed interface OrderInsuranceUiState {
    object Loading : OrderInsuranceUiState

    sealed interface HasData : OrderInsuranceUiState {
        val data: OrderInsuranceUiModel

        data class Reloading(
            override val data: OrderInsuranceUiModel
        ) : HasData

        data class Showing(
            override val data: OrderInsuranceUiModel
        ) : HasData

        object Hidden : HasData {
            override val data: OrderInsuranceUiModel = OrderInsuranceUiModel()
        }
    }
}
