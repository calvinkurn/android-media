package com.tokopedia.filter.bottomsheet.pricerangecheckbox

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetTypeFactory
import com.tokopedia.filter.common.data.Filter

internal data class PriceRangeFilterUiModel(
    val filter: Filter,
    val priceRangeList: MutableList<PriceRangeFilterItemUiModel>,
    val priceRangeLabel: String
): Visitable<SortFilterBottomSheetTypeFactory> {

    override fun type(typeFactory: SortFilterBottomSheetTypeFactory): Int {
        return typeFactory.type(this)
    }
}