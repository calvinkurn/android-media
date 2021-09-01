package com.tokopedia.tokopedianow.recentpurchase.presentation.adapter

import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductUiModel

interface RepurchaseProductGridTypeFactory {
    fun type(uiModel: RepurchaseProductUiModel): Int
}
