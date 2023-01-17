package com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.FilterRefreshable
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetTypeFactory
import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.common.data.Filter

internal data class PriceRangeFilterCheckboxDataView(
    override val filter: Filter,
    override var optionViewModelList: MutableList<OptionViewModel>,
): FilterRefreshable, Visitable<SortFilterBottomSheetTypeFactory> {

    override val willSortOptionList: Boolean
        get() = false

    override fun type(typeFactory: SortFilterBottomSheetTypeFactory): Int {
        return typeFactory.type(this)
    }
}