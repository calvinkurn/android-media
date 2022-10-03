package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel

sealed class ShipmentInfoUiState {
    object Loading : ShipmentInfoUiState()

    data class Showing(
        val data: ShipmentInfoUiModel
    ) : ShipmentInfoUiState()

    data class Error(
        val throwable: Throwable
    ) : ShipmentInfoUiState()
}

