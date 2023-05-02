package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemSeeAllUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel

interface TokoNowCategoryMenuItemTypeFactory {
    fun type(uiModel: TokoNowCategoryMenuItemUiModel): Int
    fun type(uiModel: TokoNowCategoryMenuItemSeeAllUiModel): Int
}
