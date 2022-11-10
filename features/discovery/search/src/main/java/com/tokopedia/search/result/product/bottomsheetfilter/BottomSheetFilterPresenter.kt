package com.tokopedia.search.result.product.bottomsheetfilter

interface BottomSheetFilterPresenter {
    fun getProductCount(mapParameter: Map<String, String>?)
    fun openFilterPage(searchParameter: Map<String, Any>?)
    val isBottomSheetFilterEnabled: Boolean
    fun onBottomSheetFilterDismissed()
    fun onApplySortFilter(mapParameter: Map<String, Any>)
    fun clearDynamicFilter()
}
