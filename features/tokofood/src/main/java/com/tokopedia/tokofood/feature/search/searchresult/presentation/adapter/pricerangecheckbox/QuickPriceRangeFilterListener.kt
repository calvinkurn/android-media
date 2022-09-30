package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.pricerangecheckbox

import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodOptionUiModel

interface QuickPriceRangeFilterListener {
    fun onPriceRangeFilterCheckboxItemClicked(uiModel: TokofoodOptionUiModel, isChecked: Boolean)
}