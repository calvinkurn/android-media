package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory

import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.*

interface CampaignStockTypeFactory {
    fun type(model: ActiveProductSwitchUiModel): Int
    fun type(model: TotalStockEditorUiModel): Int
    fun type(model: StockTickerInfoUiModel): Int
    fun type(model: ReservedEventInfoUiModel): Int
    fun type(model: SellableStockProductUIModel): Int
}