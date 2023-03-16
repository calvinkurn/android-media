package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop.Ticker

data class MPSShopTickerDataView(
    val type: String = "",
    val message: String = "",
) {

    fun willShow() = message.isNotEmpty()

    companion object {
        fun create(ticker: Ticker): MPSShopTickerDataView = MPSShopTickerDataView(
            type = ticker.type,
            message = ticker.message,
        )
    }
}
