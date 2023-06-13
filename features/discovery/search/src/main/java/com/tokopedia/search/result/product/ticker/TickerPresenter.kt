package com.tokopedia.search.result.product.ticker

interface TickerPresenter {
    fun onPriceFilterTickerDismissed()
    val isTickerHasDismissed: Boolean
}
