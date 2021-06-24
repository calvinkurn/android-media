package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData

object TickerMapper {
    fun mapTickerData(tickerList: List<com.tokopedia.tokomart.home.domain.model.Ticker> ): List<TickerData> {
        val uiTickerList = mutableListOf<TickerData>()
        tickerList.map { tickerData ->
            uiTickerList.add(
                    TickerData(
                            title = tickerData.title,
                            description = tickerData.message,
                            type = Ticker.TYPE_ANNOUNCEMENT
                    )
            )
        }
        return uiTickerList
    }
}