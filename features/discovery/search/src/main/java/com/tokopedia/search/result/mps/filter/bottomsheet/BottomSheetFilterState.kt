package com.tokopedia.search.result.mps.filter.bottomsheet

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.utils.mvvm.SearchUiState

data class BottomSheetFilterState(
    val isOpen: Boolean = false,
    val dynamicFilterModel: DynamicFilterModel? = null,
): SearchUiState {

    fun openBottomSheetFilter() = copy(isOpen = true)

    fun setBottomSheetFilterModel(dynamicFilterModel: DynamicFilterModel) = copy(
        dynamicFilterModel = dynamicFilterModel,
    )

    fun closeBottomSheetFilter() = copy(isOpen = false)
}
