package com.tokopedia.product.manage.feature.list.view.datasource

import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.product.manage.common.feature.getstatusshop.data.model.StatusInfo.Companion.ON_MODERATED_PERMANENTLY
import com.tokopedia.product.manage.common.feature.getstatusshop.data.model.StatusInfo.Companion.ON_MODERATED_STAGE
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import javax.inject.Inject

class TickerStaticDataProvider @Inject constructor(private val resourceProvider: ResourceProvider) {

    private fun MutableList<TickerData>.addNotifyMeTicker(isShowTickerNotifyMe: Boolean) {
        if (isShowTickerNotifyMe) {
            add(
                TickerData(
                    title = resourceProvider.getTickerNotifyMeTitle(),
                    description = resourceProvider.getTickerNotifyMeDescription(),
                    type = Ticker.TYPE_ANNOUNCEMENT,
                    isFromHtml = true
                )
            )
        }
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

    fun getTickers(multiLocationSeller: Boolean, statusShop: String, isShowTickerNotifyMe: Boolean): List<TickerData> {
        return when (statusShop.toIntSafely()) {
            ON_MODERATED_STAGE -> {
                getTickerShopModerate()
            }
            ON_MODERATED_PERMANENTLY -> {
                getTickerShopModeratePermanent()
            }
            else -> {
                mutableListOf<TickerData>().apply {
                    addMaxStockTicker()
                    addMultiLocationTicker(multiLocationSeller)
                }.filter { it.description.isNotBlank() }
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
