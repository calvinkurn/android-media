package com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory

import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetItemUiModel

interface BrandWidgetItemTypeFactory {
    fun type(uiModel: BrandWidgetItemUiModel): Int
}
