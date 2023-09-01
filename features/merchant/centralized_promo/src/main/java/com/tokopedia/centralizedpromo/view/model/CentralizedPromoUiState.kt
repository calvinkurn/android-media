package com.tokopedia.centralizedpromo.view.model


data class CentralizedPromoUiState(
    val isSwipeRefresh: Boolean = false,
    val showRbac: Boolean = false,
    val selectedTabFilterData: Pair<String, String> = Pair("", ""),
    val onGoingData: CentralizedPromoResult<BaseUiModel> = CentralizedPromoResult.Loading,
    val promoCreationData: CentralizedPromoResult<BaseUiModel> = CentralizedPromoResult.Loading
) {
    fun selectedTabId() = selectedTabFilterData.first
    fun selectedTabName() = selectedTabFilterData.second
}
