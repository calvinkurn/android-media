package com.tokopedia.ordermanagement.buyercancellationorder.common.utils

import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts
import com.tokopedia.unifycomponents.ticker.Ticker

object BuyerUtils {

    @JvmStatic
    fun getTickerType(typeStr: String): Int {
        return when (typeStr) {
            BuyerConsts.TICKER_TYPE_ERROR -> {
                Ticker.TYPE_ERROR
            }
            BuyerConsts.TICKER_TYPE_INFORMATION -> {
                Ticker.TYPE_INFORMATION
            }
            BuyerConsts.TICKER_TYPE_WARNING -> {
                Ticker.TYPE_WARNING
            }
            else -> {
                Ticker.TYPE_ANNOUNCEMENT
            }
        }
    }
}
