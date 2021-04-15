package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.TickerDataView

interface TickerListener {
    fun onTickerClicked(tickerDataView: TickerDataView)
    fun onTickerDismissed()
    val isTickerHasDismissed: Boolean
}