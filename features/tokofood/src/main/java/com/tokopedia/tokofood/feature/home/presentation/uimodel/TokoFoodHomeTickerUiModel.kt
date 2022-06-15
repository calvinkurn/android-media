package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodHomeTypeFactory
import com.tokopedia.unifycomponents.ticker.TickerData

data class TokoFoodHomeTickerUiModel (
    val id: String,
    val tickers: List<TickerData>,
): TokoFoodHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoFoodHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}