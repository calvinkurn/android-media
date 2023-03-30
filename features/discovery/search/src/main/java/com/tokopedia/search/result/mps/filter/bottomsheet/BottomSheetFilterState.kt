package com.tokopedia.search.result.mps.filter.bottomsheet

import com.tokopedia.filter.common.data.DynamicFilterModel

data class BottomSheetFilterState(
    val isBottomSheetFilterOpen: Boolean = false,
    val bottomSheetFilterModel: DynamicFilterModel? = null,
) {

    fun openBottomSheetFilter() = copy(isBottomSheetFilterOpen = true)

    fun setBottomSheetFilterModel(dynamicFilterModel: DynamicFilterModel) = copy(
        bottomSheetFilterModel = dynamicFilterModel,
    )

    fun closeBottomSheetFilter() = copy(isBottomSheetFilterOpen = false)
}
