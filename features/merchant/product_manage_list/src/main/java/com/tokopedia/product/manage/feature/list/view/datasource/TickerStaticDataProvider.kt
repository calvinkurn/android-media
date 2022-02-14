package com.tokopedia.product.manage.feature.list.view.datasource

import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import javax.inject.Inject

class TickerStaticDataProvider @Inject constructor(private val resourceProvider: ResourceProvider) {

    private fun MutableList<TickerData>.addMultiLocationTicker(multiLocationSeller: Boolean) {
        if (multiLocationSeller) {
            add(
                TickerData(
                    title = resourceProvider.getTickerMultiLocationTitle(),
                    description = resourceProvider.getTickerMultiLocationDescription(),
                    type = Ticker.TYPE_ANNOUNCEMENT,
                    isFromHtml = false
                )
            )
        }
    }

    private fun MutableList<TickerData>.addGoPayCoinsCashbackInfo() {
        add(
            TickerData(
                title = resourceProvider.getTickerGoPayCoinsCashbackInfoTitle(),
                description = resourceProvider.getTickerGoPayCoinsCashbackInfoDescription(),
                type = Ticker.TYPE_ANNOUNCEMENT,
                isFromHtml = true
            )
        )
    }

    fun getTickers(multiLocationSeller: Boolean): List<TickerData> {
        return mutableListOf<TickerData>().apply {
            addMultiLocationTicker(multiLocationSeller)
            addGoPayCoinsCashbackInfo()
        }.filter { it.description.isNotBlank() }
    }
}
