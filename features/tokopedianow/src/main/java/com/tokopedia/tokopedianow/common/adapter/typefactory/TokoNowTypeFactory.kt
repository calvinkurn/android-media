package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.*

interface TokoNowTypeFactory {
    fun type(uiModel: TokoNowCategoryGridUiModel): Int
    fun type(uiModel: TokoNowRecentPurchaseUiModel): Int
    fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int
    fun type(uiModel: TokoNowEmptyStateOocUiModel): Int
}