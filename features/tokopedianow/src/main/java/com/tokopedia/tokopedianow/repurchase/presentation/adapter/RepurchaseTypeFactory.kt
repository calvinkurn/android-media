package com.tokopedia.tokopedianow.repurchase.presentation.adapter

import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLoadingUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel

interface RepurchaseTypeFactory {

    fun type(uiModel: RepurchaseProductUiModel): Int
    fun type(uiModel: RepurchaseLoadingUiModel): Int
    fun type(uiModel: RepurchaseEmptyStateNoHistoryUiModel): Int
    fun type(uiModel: RepurchaseSortFilterUiModel): Int
}

