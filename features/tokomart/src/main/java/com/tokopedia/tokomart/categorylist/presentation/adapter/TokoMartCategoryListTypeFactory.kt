package com.tokopedia.tokomart.categorylist.presentation.adapter

import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel

interface TokoMartCategoryListTypeFactory {

    fun type(uiModel: CategoryListItemUiModel): Int
    fun type(uiModel: CategoryListChildUiModel): Int
}
