package com.tokopedia.search.result.product.inspirationwidget.size

import com.tokopedia.filter.common.data.Option

interface InspirationSizeListener {

    fun isFilterSelected(option: Option?): Boolean

    fun onInspirationSizeOptionClicked(sizeOptionDataView: InspirationSizeOptionDataView)
}