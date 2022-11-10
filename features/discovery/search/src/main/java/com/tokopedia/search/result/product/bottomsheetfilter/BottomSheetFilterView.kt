package com.tokopedia.search.result.product.bottomsheetfilter

import com.tokopedia.filter.common.data.DynamicFilterModel

interface BottomSheetFilterView {
    fun setProductCount(productCountText: String?)
    fun sendTrackingOpenFilterPage()
    fun openBottomSheetFilter(dynamicFilterModel: DynamicFilterModel?)
    fun setDynamicFilter(dynamicFilterModel: DynamicFilterModel)
}
