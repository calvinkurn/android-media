package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.filter.common.data.Option

interface InspirationSizeOptionListener {
    fun onInspirationSizeOptionClicked(option: Option, isActive: Boolean)
    fun onInspirationSizeClosed()
}