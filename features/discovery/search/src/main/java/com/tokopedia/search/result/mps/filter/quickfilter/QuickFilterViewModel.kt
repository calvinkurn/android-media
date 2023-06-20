package com.tokopedia.search.result.mps.filter.quickfilter

interface QuickFilterViewModel {

    fun onQuickFilterSelected(quickFilterDataView: QuickFilterDataView)

    fun openBottomSheetFilter()
}
