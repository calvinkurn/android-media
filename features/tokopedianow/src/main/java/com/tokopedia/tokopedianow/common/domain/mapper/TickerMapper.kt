package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData

object TickerMapper {
    private const val TICKER_TYPE_WARNING = "warning"
    private const val METADATA_TYPE_BLOCK_ADD_TO_CART = "blockAddToCart"

    fun mapTickerData(tickerList: GetTargetedTickerResponse): Pair<Boolean, List<TickerData>> {
        var blockAddToCart = false
        val uiTickerList = tickerList.getTargetedTicker?.tickers?.map { tickerData ->
            blockAddToCart = tickerData.metadata.find { it.type == METADATA_TYPE_BLOCK_ADD_TO_CART }?.values?.firstOrNull()?.toBooleanStrictOrNull().orFalse()
            TickerData(
                title = tickerData.title,
                description = tickerData.content,
                type = if (tickerData.type == TICKER_TYPE_WARNING) Ticker.TYPE_WARNING else Ticker.TYPE_ANNOUNCEMENT
            )
        }
        return Pair(blockAddToCart, uiTickerList.orEmpty())
    }
}
