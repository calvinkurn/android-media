package com.tokopedia.home.beranda.data.newatf.ticker.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.Ticker
import com.tokopedia.home.beranda.domain.model.Tickers
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import javax.inject.Inject

/**
 * Created by Frenzel
 */
class TickerMapper @Inject constructor() {
    companion object {
        private const val LAYOUT_FLOATING = "floating"
        private const val BE_TICKER_ANNOUNCEMENT = 0
        private const val BE_TICKER_INFORMATION = 1
        private const val BE_TICKER_WARNING = 2
        private const val BE_TICKER_ERROR = 3
    }

    fun asVisitable(
        data: Ticker,
        atfData: AtfData,
    ): Visitable<*>? {
        return if (!atfData.isCache) {
            if (!HomeRevampFragment.HIDE_TICKER) {
                data.tickers.filter { it.layout != LAYOUT_FLOATING }.let {
                    if (it.isNotEmpty()) TickerDataModel(tickers = mappingTickerFromServer(it))
                    else null
                }
            } else null
        } else null
    }

    private fun mappingTickerFromServer(it: List<Tickers>): List<Tickers> {
        return it.map { ticker ->
            ticker.tickerType = mapTickerType(ticker)
            ticker
        }
    }

    private fun mapTickerType(ticker: Tickers): Int = when (ticker.tickerType) {
        BE_TICKER_ANNOUNCEMENT -> com.tokopedia.unifycomponents.ticker.Ticker.TYPE_ANNOUNCEMENT
        BE_TICKER_INFORMATION -> com.tokopedia.unifycomponents.ticker.Ticker.TYPE_INFORMATION
        BE_TICKER_WARNING -> com.tokopedia.unifycomponents.ticker.Ticker.TYPE_WARNING
        BE_TICKER_ERROR -> com.tokopedia.unifycomponents.ticker.Ticker.TYPE_ERROR
        else -> com.tokopedia.unifycomponents.ticker.Ticker.TYPE_ANNOUNCEMENT
    }
}
