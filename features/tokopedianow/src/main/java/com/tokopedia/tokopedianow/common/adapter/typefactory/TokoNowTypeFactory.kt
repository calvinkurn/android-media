package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel

interface TokoNowTypeFactory {
    fun type(uiModel: TokoNowCategoryGridUiModel): Int
}