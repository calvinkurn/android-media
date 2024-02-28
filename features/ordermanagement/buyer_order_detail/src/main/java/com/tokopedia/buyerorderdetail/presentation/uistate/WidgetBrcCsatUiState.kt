package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.WidgetBrcCsatUiModel

sealed interface WidgetBrcCsatUiState {
    object Hidden : WidgetBrcCsatUiState
    object Loading : WidgetBrcCsatUiState

    sealed interface HasData : WidgetBrcCsatUiState {

        val data: WidgetBrcCsatUiModel

        data class Reloading(override val data: WidgetBrcCsatUiModel) : HasData
        data class Showing(override val data: WidgetBrcCsatUiModel) : HasData
    }
}
