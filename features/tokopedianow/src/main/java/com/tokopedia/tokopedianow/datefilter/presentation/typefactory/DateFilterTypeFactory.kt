package com.tokopedia.tokopedianow.datefilter.presentation.typefactory

import com.tokopedia.tokopedianow.datefilter.presentation.uimodel.DateFilterUiModel

interface DateFilterTypeFactory {
    fun type(uiModel: DateFilterUiModel): Int
}