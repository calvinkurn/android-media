package com.tokopedia.filter.bottomsheet.pricerangecheckbox

import com.tokopedia.filter.common.data.Option

data class PriceRangeFilterItemUiModel(
    val option: Option
) {
    var isSelected: Boolean = false
}