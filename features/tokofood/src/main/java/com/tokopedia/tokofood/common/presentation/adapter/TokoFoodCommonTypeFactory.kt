package com.tokopedia.tokofood.common.presentation.adapter

import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel

interface TokoFoodCommonTypeFactory {
    fun type(uiModel: TokoFoodProgressBarUiModel): Int
    fun type(uiModel: TokoFoodErrorStateUiModel): Int
}