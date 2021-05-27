package com.tokopedia.tokomart.home.presentation.uimodel

import com.tokopedia.tokomart.home.domain.model.Tickers
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeTypeFactory

data class HomeTickerUiModel (
        val id: String,
        val tickers: Tickers
) : TokoMartHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoMartHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}