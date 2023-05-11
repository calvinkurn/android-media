package com.tokopedia.tokopedianow.category.presentation.adapter

import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryTitleUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

interface CategoryTypeFactory {
    fun type(uiModel: CategoryTitleUiModel): Int
    fun type(uiModel: CategoryHeaderSpaceUiModel): Int
}
