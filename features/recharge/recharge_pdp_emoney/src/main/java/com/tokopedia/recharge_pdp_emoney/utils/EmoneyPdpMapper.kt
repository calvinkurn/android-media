package com.tokopedia.recharge_pdp_emoney.utils

import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData

/**
 * @author by jessica on 01/04/21
 */
object EmoneyPdpMapper {

    fun mapTopUpBillsTickersToTickersData(tickers: List<TopupBillsTicker>): List<TickerData> {
        return tickers.map { item ->
            var description: String = item.content
            if (item.actionText.isNotEmpty() && item.actionLink.isNotEmpty()) {
                description += " [${item.actionText}]{${item.actionLink}}"
            }
            TickerData(item.name, description,
                    when (item.type) {
                        TopupBillsTicker.TYPE_WARNING -> Ticker.TYPE_WARNING
                        TopupBillsTicker.TYPE_INFO -> Ticker.TYPE_INFORMATION
                        TopupBillsTicker.TYPE_SUCCESS -> Ticker.TYPE_ANNOUNCEMENT
                        TopupBillsTicker.TYPE_ERROR -> Ticker.TYPE_ERROR
                        else -> Ticker.TYPE_INFORMATION
                    }
            )
        }
    }
}