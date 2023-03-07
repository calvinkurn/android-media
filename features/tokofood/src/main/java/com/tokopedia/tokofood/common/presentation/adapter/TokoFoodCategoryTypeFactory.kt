package com.tokopedia.tokofood.common.presentation.adapter

import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryEmptyStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel

interface TokoFoodCategoryTypeFactory {
    fun type(uiModel: TokoFoodCategoryLoadingStateUiModel): Int
    fun type(uiModel: TokoFoodCategoryEmptyStateUiModel): Int
}