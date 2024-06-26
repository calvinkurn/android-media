package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener

import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2HeaderUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2ShimmerUiModel

interface CategoryL2TypeFactory {

    fun type(uiModel: CategoryL2HeaderUiModel): Int
    fun type(uiModel: CategoryL2ShimmerUiModel): Int
}
