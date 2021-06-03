package com.tokopedia.tokomart.categorylist.presentation.adapter

import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel

interface TokoMartCategoryListTypeFactory {

    fun type(uiModel: CategoryListChildUiModel): Int
}
