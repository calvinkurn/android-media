package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener

import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryDividerUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryProductListUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel

interface CategoryL2TabAdapterFactory {
    fun type(uiModel: CategoryQuickFilterUiModel): Int
    fun type(uiModel: CategoryProductListUiModel): Int
    fun type(uiModel: CategoryDividerUiModel): Int
}
