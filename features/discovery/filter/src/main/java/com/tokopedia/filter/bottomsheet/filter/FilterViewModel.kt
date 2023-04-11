package com.tokopedia.filter.bottomsheet.filter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.FilterRefreshable
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetTypeFactory
import com.tokopedia.filter.common.data.Filter

internal class FilterViewModel(
    override val filter: Filter,
    val hasSeeAllButton: Boolean,
    override var optionViewModelList: MutableList<OptionViewModel>,
): FilterRefreshable, Visitable<SortFilterBottomSheetTypeFactory> {

    override val willSortOptionList: Boolean
        get() = true

    override fun type(typeFactory: SortFilterBottomSheetTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}