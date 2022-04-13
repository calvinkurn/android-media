package com.tokopedia.tokofood.home.presentation.adapter

import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeFakeTabUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeLoadingStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeMerchantListUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeNoPinPoinUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeOutOfCoverageUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeUSPUiModel

interface TokoFoodHomeTypeFactory {
    fun type(uiModel: TokoFoodHomeLoadingStateUiModel): Int
    fun type(uiModel: TokoFoodHomeFakeTabUiModel): Int
    fun type(uiModel: TokoFoodHomeUSPUiModel): Int
    fun type(uiModel: TokoFoodHomeMerchantListUiModel): Int
    fun type(uiModel: TokoFoodHomeOutOfCoverageUiModel): Int
    fun type(uiModel: TokoFoodHomeNoPinPoinUiModel): Int
    fun type(uiModel: TokoFoodHomeIconsUiModel): Int
}