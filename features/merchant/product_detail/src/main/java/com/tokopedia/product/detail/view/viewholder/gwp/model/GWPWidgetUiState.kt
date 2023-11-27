package com.tokopedia.product.detail.view.viewholder.gwp.model

sealed interface GWPWidgetUiState {

    object Loading : GWPWidgetUiState

    object Hide : GWPWidgetUiState

    data class Show(val uiModel: GWPWidgetUiModel) : GWPWidgetUiState
}
