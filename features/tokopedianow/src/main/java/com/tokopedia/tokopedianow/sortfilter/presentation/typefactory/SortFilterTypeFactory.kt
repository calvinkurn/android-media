package com.tokopedia.tokopedianow.sortfilter.presentation.typefactory

import com.tokopedia.tokopedianow.sortfilter.presentation.uimodel.SortFilterUiModel

interface SortFilterTypeFactory {
    fun type(uiModel: SortFilterUiModel): Int
}