package com.tokopedia.product.manage.feature.list.view.datasource

import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.product.manage.common.feature.getstatusshop.data.model.StatusInfo
import com.tokopedia.product.manage.feature.list.data.model.GetTargetedTickerResponse
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import javax.inject.Inject

class TickerStaticDataProvider @Inject constructor(private val resourceProvider: ResourceProvider) {

    companion object {
        const val TICKER_INFO = "info"
        const val TICKER_WARNING = "warning"
        const val TICKER_DANGER = "danger"
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

    fun getTickers(
        multiLocationSeller: Boolean, statusShop: String,
        tickers: List<GetTargetedTickerResponse.GetTargetedTicker.TickerResponse> = emptyList(),
    ): List<TickerData> {
        return when (statusShop.toIntSafely()) {
            StatusInfo.ON_MODERATED_STAGE -> {
                getTickerShopModerate()
            }
            StatusInfo.ON_MODERATED_PERMANENTLY -> {
                getTickerShopModeratePermanent()
            }
            else -> {
                mutableListOf<TickerData>().apply {
                    addAll(
                        tickers.map {
                            val tickerType = if (it.type.equals(TICKER_INFO, ignoreCase = true)) {
                                Ticker.TYPE_ANNOUNCEMENT
                            } else if (it.type.equals(TICKER_WARNING, ignoreCase = true)) {
                                Ticker.TYPE_WARNING

                            } else if (it.type.equals(TICKER_DANGER, ignoreCase = true)) {
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
                                title = it.title.trim(),
                                description = description,
                                type = tickerType,
                                isFromHtml = true
                            )
                        }.toMutableList().apply {
                            mutableListOf<TickerData>().apply {
                                addMultiLocationTicker(multiLocationSeller)
                            }.filter { it.description.isNotBlank() }
                        }
                    )
                }

            }
        }
    }

    fun getTickers(multiLocationSeller: Boolean): List<TickerData> {
        return mutableListOf<TickerData>().apply {
            addMultiLocationTicker(multiLocationSeller)
        }.filter {
            it.description.isNotBlank()
        }
    }

    private fun getTickerShopModerate() = listOf(
        TickerData(
            title = resourceProvider.getTickerShopModeratedTitle(),
            description = resourceProvider.getTickerShopModeratedDescription(),
            type = Ticker.TYPE_WARNING,
            isFromHtml = true
        )
    )

    private fun getTickerShopModeratePermanent() = listOf(
        TickerData(
            title = resourceProvider.getTickerShopModeratedTitle(),
            description = resourceProvider.getTickerShopModeratedPermanentDescription(),
            type = Ticker.TYPE_WARNING,
            isFromHtml = true
        )
    )
}
