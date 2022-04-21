package com.tokopedia.search.result.product.emptystate

interface EmptyStateListener {
    fun onEmptyButtonClicked()
    fun onEmptySearchToGlobalSearchClicked(applink: String?)
    fun resetFilters()
}