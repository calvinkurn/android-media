package com.tokopedia.search.result.product.safesearch

import com.tokopedia.search.result.presentation.model.TickerDataView

interface SafeSearchPresenter {
    val isShowAdult: Boolean
    fun initSafeSearch()
    fun enableShowAdult()
    fun isShowAdultTicker(ticker: TickerDataView): Boolean
}
