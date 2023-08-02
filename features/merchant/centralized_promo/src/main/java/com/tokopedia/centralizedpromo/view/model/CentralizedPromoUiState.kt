package com.tokopedia.centralizedpromo.view.model

import com.tokopedia.centralizedpromo.view.LoadingType
import com.tokopedia.centralizedpromo.view.LoadingType.ALL
import com.tokopedia.centralizedpromo.view.LoadingType.PROMO_LIST
import com.tokopedia.usecase.coroutines.Result

data class CentralizedPromoUiState(
    val isLoading: LoadingType = LoadingType.NONE,
    val isSwipeRefresh: Boolean = false,
    val showRbac: Boolean = false,
    val selectedTabFilterData: Pair<String, String> = Pair("", ""),
    val onGoingData: Result<BaseUiModel>? = null,
    val promoCreationData: Result<BaseUiModel>? = null
) {
    fun selectedTabId() = selectedTabFilterData.first
    fun selectedTabName() = selectedTabFilterData.second
    fun isLoadingHeader() = isLoading == ALL
    fun isLoadingBody() = isLoading == ALL || isLoading == PROMO_LIST
}
