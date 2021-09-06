package com.tokopedia.tokopedianow.recentpurchase.presentation.adapter

import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterOnBuyingUiModel

interface RepurchaseSortFilterOnBuyingTypeFactory {
    fun type(uiModel: RepurchaseSortFilterOnBuyingUiModel): Int
}