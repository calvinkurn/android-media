package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTickerTypeFactory
import com.tokopedia.unifycomponents.ticker.TickerData

data class TokoNowTickerUiModel (
        val id: String = String.EMPTY,
        val tickers: List<TickerData>,
        val backgroundLightColor: String = String.EMPTY,
        val backgroundDarkColor: String = String.EMPTY
) : Visitable<TokoNowTickerTypeFactory> {
    override fun type(typeFactory: TokoNowTickerTypeFactory): Int {
        return typeFactory.type(this)
    }
}
