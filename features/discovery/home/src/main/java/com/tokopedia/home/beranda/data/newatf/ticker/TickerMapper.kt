package com.tokopedia.home.beranda.data.newatf.ticker

import android.util.Log
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.domain.model.Ticker
import com.tokopedia.home.beranda.domain.model.Tickers
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel

object TickerMapper {
    private const val LAYOUT_FLOATING = "floating"
    private const val BE_TICKER_ANNOUNCEMENT = 0
    private const val BE_TICKER_INFORMATION = 1
    private const val BE_TICKER_WARNING = 2
    private const val BE_TICKER_ERROR = 3

    fun Ticker.asVisitable(
        index: Int,
        isCache: Boolean
    ): Visitable<*>? {
        return if (!isCache) {
            if (!HomeRevampFragment.HIDE_TICKER) {
                tickers.filter { it.layout != LAYOUT_FLOATING }.let {
                    if (it.isNotEmpty()) {
                        val a = TickerDataModel(tickers = mappingTickerFromServer(it))
                        Log.d("atfflow", "Ticker asVisitable: 1a $a")
                        a
                    } else {
                        Log.d("atfflow", "Ticker asVisitable: 1b ")
                        null
                    }
                }
            } else {
                Log.d("atfflow", "Ticker asVisitable: 2")
                null
            }
        } else {
            Log.d("atfflow", "Ticker asVisitable: 3")
            null
        }
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
