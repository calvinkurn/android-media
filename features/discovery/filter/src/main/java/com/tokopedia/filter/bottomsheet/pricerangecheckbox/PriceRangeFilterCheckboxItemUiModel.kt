package com.tokopedia.filter.bottomsheet.pricerangecheckbox

import com.tokopedia.filter.common.data.Option

data class PriceRangeFilterCheckboxItemUiModel(
    val option: Option
) {
    var isSelected: Boolean = false
}