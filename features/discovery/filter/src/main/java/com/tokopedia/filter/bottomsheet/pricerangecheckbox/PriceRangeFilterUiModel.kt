package com.tokopedia.filter.bottomsheet.pricerangecheckbox

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetTypeFactory

internal data class PriceRangeFilterUiModel(
    val priceRangeList: List<PriceRangeFilterItemUiModel>,
    val priceRangeLabel: String
): Visitable<SortFilterBottomSheetTypeFactory> {

    override fun type(typeFactory: SortFilterBottomSheetTypeFactory): Int {
        return typeFactory.type(this)
    }
}