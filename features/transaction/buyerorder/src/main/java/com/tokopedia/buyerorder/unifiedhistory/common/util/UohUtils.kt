package com.tokopedia.buyerorder.unifiedhistory.common.util

import com.tokopedia.unifycomponents.ticker.Ticker

/**
 * Created by fwidjaja on 10/07/20.
 */
object UohUtils {
    fun getTickerType(typeStr: String): Int {
        return when (typeStr) {
            UohConsts.TICKER_TYPE_ANNOUNCEMENT -> {
                Ticker.TYPE_ANNOUNCEMENT
            }
            UohConsts.TICKER_TYPE_ERROR -> {
                Ticker.TYPE_ERROR
            }
            UohConsts.TICKER_TYPE_INFORMATION -> {
                Ticker.TYPE_INFORMATION
            }
            UohConsts.TICKER_TYPE_WARNING -> {
                Ticker.TYPE_WARNING
            }
            else -> {
                Ticker.TYPE_ANNOUNCEMENT
            }
        }
    }
}