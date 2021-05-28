package com.tokopedia.tokomart.home.presentation.uimodel

import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeTypeFactory
import com.tokopedia.unifycomponents.ticker.TickerData

data class HomeTickerUiModel (
        val id: String,
        val tickers: List<TickerData>
) : TokoMartHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoMartHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}