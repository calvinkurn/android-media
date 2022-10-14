package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel

sealed interface PaymentInfoUiState {
    object Loading : PaymentInfoUiState

    sealed interface HasData : PaymentInfoUiState {
        val data: PaymentInfoUiModel

        data class Reloading(
            override val data: PaymentInfoUiModel
        ) : HasData

        data class Showing(
            override val data: PaymentInfoUiModel
        ) : HasData
    }

    data class Error(
        val throwable: Throwable?
    ) : PaymentInfoUiState
}
