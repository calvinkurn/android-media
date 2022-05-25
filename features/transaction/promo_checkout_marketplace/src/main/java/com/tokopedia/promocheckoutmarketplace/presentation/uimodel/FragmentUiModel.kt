package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

data class FragmentUiModel(
        var uiData: UiData,
        var uiState: UiState
) {

    data class UiData(
            var pageSource: Int = 0,
            var totalBenefit: Int = 0,
            var usedPromoCount: Int = 0,
            var exception: Throwable? = null,
            var preAppliedPromoCode: List<String> = emptyList(),
            var defaultErrorMessage: String = ""
    )

    data class UiState(
            var isLoading: Boolean = false,
            var hasPreAppliedPromo: Boolean = false,
            var hasAnyPromoSelected: Boolean = false,
            var hasFailedToLoad: Boolean = false
    )

}