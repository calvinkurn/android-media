package com.tokopedia.tokofood.home.presentation.adapter

import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodProgressBarUiModel

interface TokoFoodProgressBarTypeFactory {
    fun type(uiModel: TokoFoodProgressBarUiModel): Int
}