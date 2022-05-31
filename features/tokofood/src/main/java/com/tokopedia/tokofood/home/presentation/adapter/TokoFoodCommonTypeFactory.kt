package com.tokopedia.tokofood.home.presentation.adapter

import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodProgressBarUiModel

interface TokoFoodCommonTypeFactory {
    fun type(uiModel: TokoFoodProgressBarUiModel): Int
    fun type(uiModel: TokoFoodErrorStateUiModel): Int
}