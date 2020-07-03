package com.tokopedia.filter.bottomsheet.pricefilter

import com.tokopedia.filter.common.data.Option

internal class PriceOptionViewModel(
        val option: Option,
        val position: Int
) {
    var isSelected = false
}