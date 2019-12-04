package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductTradeinDataModel(
        val type: String = "",
        val name: String = "",
        var tradeInParams: TradeInParams = TradeInParams(),
        var shouldRenderInitialData :Boolean = true
) : DynamicPDPDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name
}