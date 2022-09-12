package com.tokopedia.filter.bottomsheet.pricerangecheckbox

interface PriceRangeFilterCheckboxListener {
    fun onPriceRangeFilterCheckboxItemClicked(priceRangeFilterCheckboxItemUiModel: PriceRangeFilterCheckboxItemUiModel, isChecked: Boolean)
}