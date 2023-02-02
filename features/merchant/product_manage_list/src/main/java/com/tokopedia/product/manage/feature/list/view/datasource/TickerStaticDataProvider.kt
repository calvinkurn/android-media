package com.tokopedia.product.manage.feature.list.view.datasource

import com.tokopedia.product.manage.feature.list.data.model.GetTargetedTickerResponse
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

    fun getTickers(multiLocationSeller: Boolean): List<TickerData> {
        return mutableListOf<TickerData>().apply {
            addMultiLocationTicker(multiLocationSeller)
        }.filter {
            it.description.isNotBlank()
        }
    }

    fun createTicker(
        multiLocationSeller: Boolean,
        tickers: List<GetTargetedTickerResponse.GetTargetedTicker.TickerResponse> = emptyList(),
    ): List<TickerData> {
        return mutableListOf<TickerData>().apply {
            addAll(
                tickers.map {
                    val tickerType = if (it.type.equals("info", ignoreCase = true)) {
                        Ticker.TYPE_ANNOUNCEMENT
                    } else if (it.type.equals("warning", ignoreCase = true)) {
                        Ticker.TYPE_WARNING

                    } else if (it.type.equals("danger", ignoreCase = true)) {
                        Ticker.TYPE_ERROR

                    } else {
                        Ticker.TYPE_INFORMATION
                    }

                    val description = resourceProvider.getTickerDescriptionFormat(
                        it.content,
                        it.action?.appURL.orEmpty(),
                        it.action?.label.orEmpty()
                    )
                    TickerData(
                        title = it.title,
                        description = description,
                        type = tickerType,
                        isFromHtml = true
                    )
                }.toMutableList().apply {
                    addMultiLocationTicker(multiLocationSeller)
                }
            )
        }
    }
}
