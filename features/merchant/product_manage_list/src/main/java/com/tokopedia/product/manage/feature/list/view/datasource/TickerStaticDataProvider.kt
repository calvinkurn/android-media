package com.tokopedia.product.manage.feature.list.view.datasource

import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.product.manage.common.feature.getstatusshop.data.model.StatusInfo.Companion.ON_MODERATED_PERMANENTLY
import com.tokopedia.product.manage.common.feature.getstatusshop.data.model.StatusInfo.Companion.ON_MODERATED_STAGE
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TickerStaticDataProvider @Inject constructor(private val resourceProvider: ResourceProvider) {

    private val EXPIRED_DATE_TICKER_STOCK_AVAILABLE = "23/01/2023"

    private fun MutableList<TickerData>.addStockAvailableTicker() {
        if (!isExpiredTicker(EXPIRED_DATE_TICKER_STOCK_AVAILABLE)) {
            add(
                TickerData(
                    title = resourceProvider.getTickerStockAvailableTitle(),
                    description = resourceProvider.getTickerStockAvailableDescription(),
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

    fun getTickers(multiLocationSeller: Boolean, statusShop: String): List<TickerData> {
        return when (statusShop.toIntSafely()) {
            ON_MODERATED_STAGE -> {
                getTickerShopModerate()
            }
            ON_MODERATED_PERMANENTLY -> {
                getTickerShopModeratePermanent()
            }
            else -> {
                mutableListOf<TickerData>().apply {
                    addStockAvailableTicker()
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

    private fun isExpiredTicker(expiredDate:String) : Boolean{
        val expiredDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val expiredCalendar = expiredDateFormat.parse(expiredDate).time

        val currentDate = Calendar.getInstance().timeInMillis

        val totalDays = TimeUnit.MILLISECONDS.toDays(expiredCalendar - currentDate)

        return totalDays < 0
    }
}
