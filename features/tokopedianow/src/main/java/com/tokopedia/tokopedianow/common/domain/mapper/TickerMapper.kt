package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.common.domain.model.GetTickerData
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData

object TickerMapper {
    private const val TICKER_TYPE_WARNING = "warning"
    private const val METADATA_TYPE_BLOCK_ADD_TO_CART = "blockAddToCart"
    private const val METADATA_OOS_CATEGORY_ID = "oosCategoryIDs"

    fun mapTickerData(response: GetTargetedTickerResponse): GetTickerData {
        var blockAddToCart = false
        val tickerList = mutableListOf<TickerData>()
        val oosTickerList = mutableListOf<TickerData>()
        val oosCategoryIds = mutableListOf<String>()

        response.getTargetedTicker?.tickers?.forEach { ticker ->
            val metadata = ticker.metadata

            blockAddToCart = metadata.find { it.type == METADATA_TYPE_BLOCK_ADD_TO_CART }
                ?.values?.firstOrNull()?.toBooleanStrictOrNull().orFalse()

            val oosCategoryIdList = metadata.find { it.type == METADATA_OOS_CATEGORY_ID }
                ?.values.orEmpty()

            val tickerData = TickerData(
                title = ticker.title,
                description = ticker.content,
                type = if (ticker.type == TICKER_TYPE_WARNING) Ticker.TYPE_WARNING else Ticker.TYPE_ANNOUNCEMENT
            )

            if(oosCategoryIdList.isNotEmpty()) {
                oosCategoryIds.addAll(oosCategoryIdList)
                oosTickerList.add(tickerData)
            } else {
                tickerList.add(tickerData)
            }
        }

        return GetTickerData(
            blockAddToCart = blockAddToCart,
            tickerList = tickerList,
            oosTickerList = oosTickerList,
            oosCategoryIds = oosCategoryIds
        )
    }
}
