package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel

sealed interface ActionButtonsUiState {
    object Loading : ActionButtonsUiState

    sealed interface HasData : ActionButtonsUiState {
        val data: ActionButtonsUiModel

        data class Reloading(
            override val data: ActionButtonsUiModel
        ) : HasData

        data class Showing(
            override val data: ActionButtonsUiModel
        ) : HasData
    }

    data class Error(
        val throwable: Throwable?
    ) : ActionButtonsUiState
}
