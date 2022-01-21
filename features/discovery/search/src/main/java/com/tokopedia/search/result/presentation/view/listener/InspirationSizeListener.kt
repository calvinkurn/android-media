package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.filter.common.data.Option
import com.tokopedia.search.result.presentation.model.InspirationSizeOptionDataView

interface InspirationSizeOptionListener {
    fun onInspirationSizeOptionClicked(sizeOptionDataView: InspirationSizeOptionDataView, option: Option)
}