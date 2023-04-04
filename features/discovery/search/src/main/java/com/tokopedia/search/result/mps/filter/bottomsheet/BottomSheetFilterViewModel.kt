package com.tokopedia.search.result.mps.filter.bottomsheet

interface BottomSheetFilterViewModel {

    val paramater: Map<String, String>

    fun applyFilter(parameter: Map<String, String>)

    fun closeBottomSheetFilter()
}
