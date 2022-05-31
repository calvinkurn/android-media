package com.tokopedia.tokofood.home.presentation.adapter

import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel

interface TokoFoodCategoryTypeFactory {
    fun type(uiModel: TokoFoodCategoryLoadingStateUiModel): Int
}