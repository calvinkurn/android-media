package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory

import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.ActiveProductSwitchUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.TotalStockEditorUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.VariantStockEditorUiModel

interface CampaignStockTypeFactory {
    fun type(model: ActiveProductSwitchUiModel): Int
    fun type(model: TotalStockEditorUiModel): Int
    fun type(model: VariantStockEditorUiModel): Int
}