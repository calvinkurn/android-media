package com.tokopedia.filter.bottomsheet.pricerangecheckbox

data class PriceRangeFilterItemUiModel(
    val priceText: String,
    val priceRangeLevel: Int,
    val isNew: Boolean
) {
    var isSelected: Boolean = false
}