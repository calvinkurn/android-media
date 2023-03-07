package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel

sealed interface ShipmentInfoUiState {
    object Loading : ShipmentInfoUiState

    sealed interface HasData : ShipmentInfoUiState {
        val data: ShipmentInfoUiModel

        data class Reloading(
            override val data: ShipmentInfoUiModel
        ) : HasData

        data class Showing(
            override val data: ShipmentInfoUiModel
        ) : HasData
    }

    data class Error(
        val throwable: Throwable?
    ) : ShipmentInfoUiState
}

