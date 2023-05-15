package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener

import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryTitleUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel

interface CategoryTypeFactory {
    fun type(uiModel: CategoryTitleUiModel): Int
    fun type(uiModel: CategoryHeaderSpaceUiModel): Int
    fun type(uiModel: CategoryNavigationUiModel): Int
    fun type(uiModel: CategoryShowcaseUiModel): Int
}
