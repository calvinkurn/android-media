package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowCategoryChipsUiModel

interface TokoNowCategoryChipsTypeFactory {
    fun type(uiModel: TokoNowCategoryChipsUiModel): Int
}