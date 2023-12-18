package com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory

import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetItemUiModel
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetSeeAllUiModel

interface BrandWidgetItemTypeFactory {
    fun type(uiModel: BrandWidgetItemUiModel): Int
    fun type(uiModel: BrandWidgetSeeAllUiModel): Int
}
