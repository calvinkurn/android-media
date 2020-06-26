package com.tokopedia.filter.bottomsheet.pricefilter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetTypeFactory
import com.tokopedia.filter.common.data.Filter

internal class PriceFilterViewModel(
        val priceFilter: Filter,
        val minPriceFilterTitle: String,
        val maxPriceFilterTitle: String,
        var minPriceFilterValue: String,
        var maxPriceFilterValue: String,
        val priceRangeOptionViewModelList: MutableList<PriceOptionViewModel>
): Visitable<SortFilterBottomSheetTypeFactory> {

    override fun type(typeFactory: SortFilterBottomSheetTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}