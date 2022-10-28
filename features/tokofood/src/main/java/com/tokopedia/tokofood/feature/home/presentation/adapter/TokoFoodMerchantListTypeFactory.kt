package com.tokopedia.tokofood.feature.home.presentation.adapter

import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodMerchantListUiModel

interface TokoFoodMerchantListTypeFactory {
    fun type(uiModel: TokoFoodMerchantListUiModel): Int
}