package com.tokopedia.product.manage.feature.list.view.datasource

import com.tokopedia.product.manage.common.feature.getstatusshop.data.model.ShopInfo
import com.tokopedia.product.manage.feature.list.view.model.ShopStatusUIModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import javax.inject.Inject

class TickerStaticDataProvider @Inject constructor(private val resourceProvider: ResourceProvider) {

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

    fun getTickers(multiLocationSeller: Boolean): List<TickerData> {
        return mutableListOf<TickerData>().apply {
            addMaxStockTicker()
            addMultiLocationTicker(multiLocationSeller)
        }.filter { it.description.isNotBlank() }
    }

    fun getTickerShopModerate() = listOf(
        TickerData(
            title = resourceProvider.getTickerShopModeratedTitle(),
            description = resourceProvider.getTickerShopModeratedDescription(),
            type = Ticker.TYPE_WARNING,
            isFromHtml = true
        )
    )

    fun getTickerShopModeratePermanent() = listOf(
        TickerData(
            title = resourceProvider.getTickerShopModeratedTitle(),
            description = resourceProvider.getTickerShopModeratedPermanentDescription(),
            type = Ticker.TYPE_WARNING,
            isFromHtml = true
        )
    )
}
