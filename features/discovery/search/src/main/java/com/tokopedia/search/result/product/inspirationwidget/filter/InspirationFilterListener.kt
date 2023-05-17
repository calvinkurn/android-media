package com.tokopedia.search.result.product.inspirationwidget.filter

import com.tokopedia.filter.common.data.Option

interface InspirationFilterListener {
    fun isFilterSelected(optionList: List<Option>): Boolean

    fun onInspirationFilterOptionClicked(
        filterOptionDataView: InspirationFilterOptionDataView,
        filterDataView: InspirationFilterDataView,
    )
}
