package com.tokopedia.common.travel.ticker

import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

/**
 * @author by furqan on 25/02/19
 */
object TravelTickerUtils {
    private const val ANNOUNCEMENT_TYPE = 1
    private const val WARNING_TYPE = 2

    fun buildUnifyTravelTicker(travelTickerModel: TravelTickerModel, tickerView: Ticker) {
        tickerView.setHtmlDescription(travelTickerModel.message)
        tickerView.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (linkUrl.isNotEmpty()) {
                    RouteManager.route(tickerView.context, linkUrl.toString())
                }
            }

            override fun onDismiss() {}

        })
        when (travelTickerModel.type) {
            ANNOUNCEMENT_TYPE -> {
                tickerView.tickerType = Ticker.TYPE_ANNOUNCEMENT
            }
            WARNING_TYPE -> {
                tickerView.tickerType = Ticker.TYPE_WARNING
            }
        }
    }
}
