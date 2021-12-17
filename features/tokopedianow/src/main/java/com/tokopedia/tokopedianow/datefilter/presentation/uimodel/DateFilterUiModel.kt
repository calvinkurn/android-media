package com.tokopedia.tokopedianow.datefilter.presentation.uimodel

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.datefilter.presentation.adapter.DateFilterTypeFactory

data class DateFilterUiModel(
    @StringRes var titleRes: Int? = null,
    var isChecked: Boolean = false,
    var isLastItem: Boolean = false,
    var startDate: String = "",
    var endDate: String = ""
) : Visitable<DateFilterTypeFactory> {
    override fun type(typeFactory: DateFilterTypeFactory): Int {
        return typeFactory.type(this)
    }
}