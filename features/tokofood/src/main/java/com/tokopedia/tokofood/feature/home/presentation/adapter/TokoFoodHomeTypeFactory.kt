package com.tokopedia.tokofood.feature.home.presentation.adapter

import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeChooseAddressWidgetUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeLoadingStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeMerchantTitleUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeTickerUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeUSPUiModel

interface TokoFoodHomeTypeFactory {
    fun type(uiModel: TokoFoodHomeLoadingStateUiModel): Int
    fun type(uiModel: TokoFoodHomeUSPUiModel): Int
    fun type(uiModel: TokoFoodHomeEmptyStateLocationUiModel): Int
    fun type(uiModel: TokoFoodHomeIconsUiModel): Int
    fun type(uiModel: TokoFoodHomeChooseAddressWidgetUiModel): Int
    fun type(uiModel: TokoFoodHomeMerchantTitleUiModel): Int
    fun type(uiModel: TokoFoodHomeTickerUiModel): Int
}