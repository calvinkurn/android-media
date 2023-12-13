package com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory

import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel

interface BrandWidgetTypeFactory {
    fun type(uiModel: BrandWidgetUiModel): Int
}
