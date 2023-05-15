package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener

import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel

interface CategoryShowcaseTypeFactory {
    fun type(uiModel: CategoryShowcaseItemUiModel): Int
}
