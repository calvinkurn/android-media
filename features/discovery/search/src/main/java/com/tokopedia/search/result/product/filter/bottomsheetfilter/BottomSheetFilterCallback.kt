package com.tokopedia.search.result.product.filter.bottomsheetfilter

interface BottomSheetFilterCallback {
    fun getProductCount(mapParameter: Map<String, String>?)
    fun onBottomSheetFilterDismissed()
    fun onApplySortFilter(mapParameter: Map<String, Any>)
}
