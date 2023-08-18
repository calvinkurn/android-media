package com.tokopedia.search.result.product.filter.bottomsheetfilter

interface BottomSheetFilterPresenter :
    BottomSheetFilterCallback {
    fun openFilterPage(searchParameter: Map<String, Any>?)
    fun openSortPage(searchParameter: Map<String, Any>?)

    val isBottomSheetFilterEnabled: Boolean
    fun clearDynamicFilter()
}
