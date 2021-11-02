package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel

interface TokoNowCategoryItemTypeFactory {
    fun type(uiModel: TokoNowCategoryItemUiModel): Int
}