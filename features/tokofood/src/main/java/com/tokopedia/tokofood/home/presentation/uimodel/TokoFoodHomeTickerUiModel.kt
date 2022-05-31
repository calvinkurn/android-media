package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeTypeFactory
import com.tokopedia.unifycomponents.ticker.TickerData

class TokoFoodHomeTickerUiModel (
    val id: String,
    val tickers: List<TickerData>,
    @TokoFoodLayoutState val state: Int
): TokoFoodHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoFoodHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}