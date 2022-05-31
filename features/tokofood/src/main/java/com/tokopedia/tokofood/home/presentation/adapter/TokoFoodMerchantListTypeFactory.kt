package com.tokopedia.tokofood.home.presentation.adapter

import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodMerchantListUiModel

interface TokoFoodMerchantListTypeFactory {
    fun type(uiModel: TokoFoodMerchantListUiModel): Int
}