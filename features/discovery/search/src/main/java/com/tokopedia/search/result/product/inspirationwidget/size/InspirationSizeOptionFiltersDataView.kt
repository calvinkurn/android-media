package com.tokopedia.search.result.product.inspirationwidget.size

import com.tokopedia.filter.common.data.Option

data class InspirationSizeOptionFiltersDataView(
    val key: String = "",
    val name: String = "",
    val value: String = "",
) {

    val option = Option(key = key, value = value, name = name)
}