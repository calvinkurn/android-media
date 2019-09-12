package com.tokopedia.discovery.categoryrevamp.view.interfaces

import com.tokopedia.filter.common.data.Option


interface QuickFilterListener {
    fun onQuickFilterSelected(option: Option)

    fun isQuickFilterSelected(option: Option): Boolean
}