package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.filter.common.data.Option
import com.tokopedia.search.result.presentation.model.SizeOptionDataView

interface InspirationSizeOptionListener {
    fun onInspirationSizeOptionClicked(sizeOptionDataView: SizeOptionDataView, option: Option, isActive: Boolean)
}