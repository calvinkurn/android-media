package com.tokopedia.filter.bottomsheet.pricerangecheckbox

interface PriceRangeFilterListener {
    fun onPriceRangeItemClicked(priceRangeFilterItemUiModel: PriceRangeFilterItemUiModel, isChecked: Boolean)
}