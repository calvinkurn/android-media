package com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox

import com.tokopedia.filter.bottomsheet.FilterRefreshable
import com.tokopedia.filter.bottomsheet.filter.OptionViewModel

internal interface PriceRangeFilterCheckboxListener {
    fun onPriceRangeFilterCheckboxItemClicked(filterRefreshable: FilterRefreshable, optionViewModel: OptionViewModel)
}