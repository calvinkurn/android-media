package com.tokopedia.product.manage.feature.campaignstock.ui.dataview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory

class StockTickerInfoUiModel(val isReserved: Boolean): Visitable<CampaignStockTypeFactory> {

    override fun type(typeFactory: CampaignStockTypeFactory): Int =
            typeFactory.type(this)
}