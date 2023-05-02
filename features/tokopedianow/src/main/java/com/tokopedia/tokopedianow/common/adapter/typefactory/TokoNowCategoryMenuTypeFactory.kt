package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel

interface TokoNowCategoryMenuTypeFactory {
    fun type(uiModel: TokoNowCategoryMenuUiModel): Int
}
