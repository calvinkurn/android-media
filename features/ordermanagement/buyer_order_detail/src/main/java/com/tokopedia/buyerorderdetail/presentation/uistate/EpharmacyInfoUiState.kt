package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.EpharmacyInfoUiModel

sealed interface EpharmacyInfoUiState {
    object Loading : EpharmacyInfoUiState

    sealed interface HasData : EpharmacyInfoUiState {
        val data: EpharmacyInfoUiModel

        data class Reloading(
            override val data: EpharmacyInfoUiModel
        ) : HasData

        data class Showing(
            override val data: EpharmacyInfoUiModel
        ) : HasData
    }

    data class Error(
        val throwable: Throwable?
    ) : EpharmacyInfoUiState
}
