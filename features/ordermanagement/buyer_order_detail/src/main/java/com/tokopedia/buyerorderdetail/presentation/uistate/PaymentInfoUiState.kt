package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel

sealed class PaymentInfoUiState {
    object Loading : PaymentInfoUiState()

    data class Showing(
        val data: PaymentInfoUiModel
    ) : PaymentInfoUiState()

    data class Error(
        val throwable: Throwable
    ) : PaymentInfoUiState()
}
