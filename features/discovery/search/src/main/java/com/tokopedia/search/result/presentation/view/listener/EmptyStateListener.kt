package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.filter.common.data.Option

interface EmptyStateListener {
    fun onEmptyButtonClicked()
    fun onSelectedFilterRemoved(uniqueId: String?)
    fun getRegistrationId(): String
    fun getUserId(): String
    fun getSelectedFilterAsOptionList(): List<Option>?
    fun onEmptySearchToGlobalSearchClicked(applink: String?)
}