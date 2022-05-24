package com.tokopedia.tokofood.home.presentation.adapter

import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeChooseAddressWidgetUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeLoadingStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeUSPUiModel

interface TokoFoodHomeTypeFactory {
    fun type(uiModel: TokoFoodHomeLoadingStateUiModel): Int
    fun type(uiModel: TokoFoodHomeUSPUiModel): Int
    fun type(uiModel: TokoFoodHomeEmptyStateLocationUiModel): Int
    fun type(uiModel: TokoFoodHomeIconsUiModel): Int
    fun type(uiModel: TokoFoodHomeChooseAddressWidgetUiModel): Int
}