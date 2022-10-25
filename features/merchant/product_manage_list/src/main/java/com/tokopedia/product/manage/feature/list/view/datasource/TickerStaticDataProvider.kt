package com.tokopedia.product.manage.feature.list.view.datasource

import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import javax.inject.Inject

class TickerStaticDataProvider @Inject constructor(private val resourceProvider: ResourceProvider) {


    private fun MutableList<TickerData>.addNotifyMeTicker() {
        add(
            TickerData(
                title = resourceProvider.getTickerNotifyMeTitle(),
                description = resourceProvider.getTickerNotifyMeDescription(),
                type = Ticker.TYPE_ANNOUNCEMENT,
                isFromHtml = true
            )
        )
    }

    private fun MutableList<TickerData>.addMaxStockTicker() {
        add(
            TickerData(
                title = resourceProvider.getTickerMaxStockTitle(),
                description = resourceProvider.getTickerMaxStockDescription(),
                type = Ticker.TYPE_ANNOUNCEMENT,
                isFromHtml = false
            )
        )
    }
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

    fun getTickers(multiLocationSeller: Boolean, isShowTickerNotifyMe: Boolean = false): List<TickerData> {
        return mutableListOf<TickerData>().apply {
            if (isShowTickerNotifyMe){
                addNotifyMeTicker()
            }
            addMaxStockTicker()
            addMultiLocationTicker(multiLocationSeller)
        }.filter { it.description.isNotBlank() }
    }

    fun getTickers(multiLocationSeller: Boolean): List<TickerData> {
        return mutableListOf<TickerData>().apply {
            addMultiLocationTicker(multiLocationSeller)
        }.filter { it.description.isNotBlank() }
    }
}
