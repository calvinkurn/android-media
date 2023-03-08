package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.home.domain.model.GetTargetedTickerResponse
import com.tokopedia.usercomponents.stickylogin.common.StickyLoginConstant
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData

object TickerMapper {
    fun mapTickerData(tickerList: List<com.tokopedia.tokopedianow.home.domain.model.Ticker>): List<TickerData> {
        val uiTickerList = mutableListOf<TickerData>()
        tickerList.map { tickerData ->
            if (tickerData.layout == StickyLoginConstant.LAYOUT_DEFAULT) {
                uiTickerList.add(
                    TickerData(
                        title = tickerData.title,
                        description = tickerData.message,
                        type = Ticker.TYPE_ANNOUNCEMENT
                    )
                )
            }
        }
        return uiTickerList
    }

    fun mapTickerData(tickerList: GetTargetedTickerResponse): List<TickerData> {
        val uiTickerList = mutableListOf<TickerData>()
        tickerList.getTargetedTicker?.tickers?.map { tickerData ->
            uiTickerList.add(
                TickerData(
                    title = tickerData.title,
                    description = tickerData.content,
                    type = Ticker.TYPE_ANNOUNCEMENT
                )
            )
        }
        return uiTickerList
    }
}
