package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTickerTypeFactory
import com.tokopedia.unifycomponents.ticker.TickerData

data class TokoNowTickerUiModel (
        val id: String,
        val tickers: List<TickerData>
) : Visitable<TokoNowTickerTypeFactory> {
    override fun type(typeFactory: TokoNowTickerTypeFactory): Int {
        return typeFactory.type(this)
    }
}
