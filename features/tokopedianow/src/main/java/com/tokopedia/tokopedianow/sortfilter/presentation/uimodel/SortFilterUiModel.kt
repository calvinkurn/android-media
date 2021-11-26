package com.tokopedia.tokopedianow.sortfilter.presentation.uimodel

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.sortfilter.presentation.typefactory.SortFilterTypeFactory

data class SortFilterUiModel(
    @StringRes var titleRes: Int,
    var isChecked: Boolean,
    var isLastItem: Boolean,
    var value: Int
) : Visitable<SortFilterTypeFactory> {
    override fun type(typeFactory: SortFilterTypeFactory): Int {
        return typeFactory.type(this)
    }
}