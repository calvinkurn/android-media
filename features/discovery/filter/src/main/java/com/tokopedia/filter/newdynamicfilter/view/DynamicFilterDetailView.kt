package com.tokopedia.filter.newdynamicfilter.view

import com.tokopedia.filter.common.data.Option

interface DynamicFilterDetailView {
    fun onItemCheckedChanged(option: Option, isChecked: Boolean)
}
