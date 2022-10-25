package com.tokopedia.search.result.product.ticker

import javax.inject.Inject

class TickerPresenterDelegate @Inject constructor() : TickerPresenter {
    override var isTickerHasDismissed = false
        private set

    override fun onPriceFilterTickerDismissed() {
        isTickerHasDismissed = true
    }
}
