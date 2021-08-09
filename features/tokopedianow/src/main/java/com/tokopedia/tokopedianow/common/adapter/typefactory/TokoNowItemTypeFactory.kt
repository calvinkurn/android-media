package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel

interface TokoNowItemTypeFactory {
    fun type(uiModel: TokoNowCategoryItemUiModel): Int
}