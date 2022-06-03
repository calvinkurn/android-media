package com.tokopedia.tokofood.feature.home.presentation.adapter

import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel

interface TokoFoodCategoryTypeFactory {
    fun type(uiModel: TokoFoodCategoryLoadingStateUiModel): Int
}