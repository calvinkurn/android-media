package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.SizeOptionDataView

interface InspirationSizeOptionListener {
    fun onInspirationSizeOptionClicked(optionData: SizeOptionDataView)
    fun onInspirationSizeClosed()
}