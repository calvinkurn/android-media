package com.tokopedia.tokopedianow.recentpurchase.presentation.adapter

import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseLoadingUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductGridUiModel

interface RecentPurchaseTypeFactory {

    fun type(uiModel: RepurchaseProductGridUiModel): Int
    fun type(uiModel: RepurchaseLoadingUiModel): Int
    fun type(uiModel: RepurchaseEmptyStateNoHistoryUiModel): Int
}

