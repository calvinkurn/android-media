package com.tokopedia.tokopedianow.datefilter.presentation.adapter

import com.tokopedia.tokopedianow.datefilter.presentation.uimodel.DateFilterUiModel

interface DateFilterTypeFactory {
    fun type(uiModel: DateFilterUiModel): Int
}