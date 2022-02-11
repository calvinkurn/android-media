package com.tokopedia.search.result.product.emptystate

import com.tokopedia.filter.common.data.Option

interface EmptyStateListener {
    fun onEmptyButtonClicked()
    fun onSelectedFilterRemoved(uniqueId: String?)
    fun getSelectedFilterAsOptionList(): List<Option>?
    fun onEmptySearchToGlobalSearchClicked(applink: String?)
}