package com.tokopedia.centralizedpromo.view.model

import com.tokopedia.centralizedpromo.view.PromoCreationMapper.TAB_ID_ALL_FEATURE
import com.tokopedia.centralizedpromo.view.PromoCreationMapper.TAB_NAME_ALL_FEATURE

data class CentralizedPromoUiState(
    val isSwipeRefresh: Boolean = false,
    val hasUpdatedFilter: Boolean = false,
    val showRbac: Boolean = false,
    val selectedTabFilterData: Pair<String, String> = Pair(TAB_ID_ALL_FEATURE, TAB_NAME_ALL_FEATURE),
    val onGoingData: CentralizedPromoResult<BaseUiModel> = CentralizedPromoResult.Loading,
    val promoCreationData: CentralizedPromoResult<BaseUiModel> = CentralizedPromoResult.Loading
) {
    fun selectedTabId() = selectedTabFilterData.first
    fun selectedTabName() = selectedTabFilterData.second
}
