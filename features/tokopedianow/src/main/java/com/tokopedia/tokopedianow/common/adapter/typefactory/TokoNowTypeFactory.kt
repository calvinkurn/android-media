package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecentPurchaseUiModel

interface TokoNowTypeFactory {
    fun type(uiModel: TokoNowCategoryGridUiModel): Int
    fun type(uiModel: TokoNowRecentPurchaseUiModel): Int
}