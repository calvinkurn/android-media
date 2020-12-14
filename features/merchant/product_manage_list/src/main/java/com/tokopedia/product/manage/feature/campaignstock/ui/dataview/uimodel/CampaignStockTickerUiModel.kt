package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import com.tokopedia.unifycomponents.ticker.TickerData

data class CampaignStockTickerUiModel(
    val tickerList: List<TickerData>
): Visitable<CampaignStockTypeFactory> {

    override fun type(typeFactory: CampaignStockTypeFactory): Int {
       return typeFactory.type(this)
    }
}