package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory

class TotalStockEditorUiModel(val totalStock: Int): Visitable<CampaignStockTypeFactory> {

    override fun type(typeFactory: CampaignStockTypeFactory): Int =
            typeFactory.type(this)

}