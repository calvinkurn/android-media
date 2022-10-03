package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel

sealed class ActionButtonsUiState {
    object Loading : ActionButtonsUiState()

    data class Showing(
        val data: ActionButtonsUiModel
    ) : ActionButtonsUiState()

    data class Error(
        val throwable: Throwable
    ) : ActionButtonsUiState()
}
