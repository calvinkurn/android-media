package com.tokopedia.discovery.categoryrevamp.view.interfaces

import com.tokopedia.filter.common.data.Option

interface SelectedFilterListener {
    fun onSelectedFilterRemoved(uniqueId: String)
    fun getSelectedFilterAsOptionList(): List<Option>?
}
