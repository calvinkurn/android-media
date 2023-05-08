package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

data class BoInfoBottomSheetUiModel(
    val uiData: UiData = UiData(),
    val uiState: UiState = UiState()
) {

    data class UiData(
        val title: String = "",
        val imageUrl: String = "",
        val contentTitle: String = "",
        val contentDescription: String = "",
        val buttonText: String = ""
    )

    data class UiState(
        val isVisible: Boolean = false
    )
}
