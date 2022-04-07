package com.tokopedia.tokofood.home.presentation.adapter

import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodFakeTabUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodIconsUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodLoadingStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodMerchantListUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodNoPinPoinUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodOutOfCoverageUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodUSPUiModel

interface TokoFoodHomeTypeFactory {
    fun type(uiModel: TokoFoodLoadingStateUiModel): Int
    fun type(uiModel: TokoFoodFakeTabUiModel): Int
    fun type(uiModel: TokoFoodUSPUiModel): Int
    fun type(uiModel: TokoFoodMerchantListUiModel): Int
    fun type(uiModel: TokoFoodOutOfCoverageUiModel): Int
    fun type(uiModel: TokoFoodNoPinPoinUiModel): Int
    fun type(uiModel: TokoFoodIconsUiModel): Int
}