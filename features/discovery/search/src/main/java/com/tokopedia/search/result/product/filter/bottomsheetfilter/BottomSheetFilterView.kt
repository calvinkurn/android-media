package com.tokopedia.search.result.product.filter.bottomsheetfilter

import com.tokopedia.filter.common.data.DynamicFilterModel

interface BottomSheetFilterView {
    fun setProductCount(productCountText: String?)
    fun sendTrackingOpenFilterPage()
    fun openBottomSheetFilter(
        dynamicFilterModel: DynamicFilterModel?,
        callback: BottomSheetFilterCallback,
    )
    fun openBottomSheetSort(
        dynamicFilterModel: DynamicFilterModel?,
        callback: BottomSheetFilterCallback,
    )
    fun setDynamicFilter(dynamicFilterModel: DynamicFilterModel)
}
