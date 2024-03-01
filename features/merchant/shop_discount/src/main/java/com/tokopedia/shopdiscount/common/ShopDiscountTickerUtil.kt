package com.tokopedia.shopdiscount.common

import com.tokopedia.campaign.utils.constant.TickerType
import com.tokopedia.unifycomponents.ticker.Ticker

object ShopDiscountTickerUtil {
    fun getTickerType(tickerType: String): Int {
        return when (tickerType.lowercase()) {
            TickerType.INFO -> Ticker.TYPE_ANNOUNCEMENT
            TickerType.WARNING -> Ticker.TYPE_WARNING
            TickerType.DANGER -> Ticker.TYPE_ERROR
            else -> Ticker.TYPE_INFORMATION
        }
    }
}
