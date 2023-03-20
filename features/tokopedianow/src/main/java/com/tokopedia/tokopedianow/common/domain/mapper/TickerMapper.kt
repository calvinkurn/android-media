package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData

object TickerMapper {
    private const val TYPE_WARNING = "warning"

    fun mapTickerData(tickerList: GetTargetedTickerResponse): List<TickerData> {
        val uiTickerList = mutableListOf<TickerData>()
        tickerList.getTargetedTicker?.tickers?.map { tickerData ->
            uiTickerList.add(
                TickerData(
                    title = tickerData.title,
                    description = tickerData.content,
                    type = if (tickerData.type == TYPE_WARNING) Ticker.TYPE_WARNING else Ticker.TYPE_ANNOUNCEMENT
                )
            )
        }
        return uiTickerList
    }
}
