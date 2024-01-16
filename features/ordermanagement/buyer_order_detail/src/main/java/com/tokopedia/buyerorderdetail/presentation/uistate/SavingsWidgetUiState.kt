package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.SavingsWidgetUiModel

sealed interface SavingsWidgetUiState {
    object Nothing : SavingsWidgetUiState

    object Hide : SavingsWidgetUiState

    data class Success(val data: SavingsWidgetUiModel) : SavingsWidgetUiState

    data class Error(
        val throwable: Throwable?
    ) : SavingsWidgetUiState
}