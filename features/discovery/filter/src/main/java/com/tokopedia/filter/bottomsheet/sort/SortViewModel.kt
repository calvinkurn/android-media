package com.tokopedia.filter.bottomsheet.sort

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetTypeFactory

internal class SortViewModel(
        val sortItemViewModelList: MutableList<SortItemViewModel>
): Visitable<SortFilterBottomSheetTypeFactory> {

    override fun type(typeFactory: SortFilterBottomSheetTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}