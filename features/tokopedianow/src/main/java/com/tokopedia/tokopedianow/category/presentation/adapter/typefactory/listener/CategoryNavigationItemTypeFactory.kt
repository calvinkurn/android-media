package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener

import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemSeeAllUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel

interface CategoryNavigationItemTypeFactory {
    fun type(uiModel: CategoryNavigationItemUiModel): Int
}
