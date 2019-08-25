package com.tokopedia.discovery.categoryrevamp.view.interfaces

import com.tokopedia.discovery.common.data.Option

interface QuickFilterListener {
    fun onQuickFilterSelected(option: Option)

    fun isQuickFilterSelected(option: Option): Boolean
}