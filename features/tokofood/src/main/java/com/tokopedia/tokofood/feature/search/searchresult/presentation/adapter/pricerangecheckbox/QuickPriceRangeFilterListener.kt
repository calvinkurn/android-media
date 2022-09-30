package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.pricerangecheckbox

import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeFilterCheckboxItemUiModel

interface QuickPriceRangeFilterListener {
    fun onPriceRangeFilterCheckboxItemClicked(uiModel: PriceRangeFilterCheckboxItemUiModel, isChecked: Boolean)
}