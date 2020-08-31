package com.tokopedia.filter.bottomsheet.filter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetTypeFactory
import com.tokopedia.filter.common.data.Filter

internal class FilterViewModel(
        val filter: Filter,
        val hasSeeAllButton: Boolean,
        var optionViewModelList: MutableList<OptionViewModel>
): Visitable<SortFilterBottomSheetTypeFactory> {

    override fun type(typeFactory: SortFilterBottomSheetTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}