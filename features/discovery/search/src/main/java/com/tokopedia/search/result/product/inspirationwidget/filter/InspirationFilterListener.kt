package com.tokopedia.search.result.product.inspirationwidget.filter

import com.tokopedia.filter.common.data.Option

interface InspirationFilterListener {

    fun isFilterSelected(option: Option?): Boolean

    fun onInspirationFilterOptionClicked(sizeOptionDataView: InspirationFilterOptionDataView)
}
