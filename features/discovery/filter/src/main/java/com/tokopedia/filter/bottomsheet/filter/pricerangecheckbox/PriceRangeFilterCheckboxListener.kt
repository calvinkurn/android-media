package com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox

import com.tokopedia.filter.bottomsheet.filter.OptionViewModel

interface PriceRangeFilterCheckboxListener {
    fun onPriceRangeFilterCheckboxItemClicked(optionViewModel: OptionViewModel, isChecked: Boolean)
}