package com.tokopedia.tokopedianow.repurchase.presentation.adapter

import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel

interface RepurchaseProductGridTypeFactory {
    fun type(uiModel: RepurchaseProductUiModel): Int
}
