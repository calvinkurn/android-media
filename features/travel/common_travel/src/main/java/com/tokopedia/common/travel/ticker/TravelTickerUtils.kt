package com.tokopedia.common.travel.ticker

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.design.component.ticker.TickerView
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import java.util.*

/**
 * @author by furqan on 25/02/19
 */
object TravelTickerUtils {
    private const val ANNOUNCEMENT_TYPE = 1
    private const val WARNING_TYPE = 2

    fun buildTravelTicker(context: Context?, travelTickerModel: TravelTickerModel, tickerView: TickerView) {
        val messages = ArrayList<String>()
        messages.add(travelTickerModel.message)
        tickerView.setListMessage(messages)
        if (context != null) {
            if (travelTickerModel.type == ANNOUNCEMENT_TYPE) {
                tickerView.setHighLightColor(ContextCompat.getColor(context, R.color.tkpd_main_green))
                tickerView.setPageIndicatorOnColor(ContextCompat.getColor(context, R.color.light_green))
                tickerView.setPageIndicatorOffColor(ContextCompat.getColor(context, R.color.light_green))
            } else if (travelTickerModel.type == WARNING_TYPE) {
                tickerView.setHighLightColor(ContextCompat.getColor(context, R.color.snackbar_border_error))
                tickerView.setPageIndicatorOnColor(ContextCompat.getColor(context, R.color.colorPink))
                tickerView.setPageIndicatorOffColor(ContextCompat.getColor(context, R.color.colorPink))
                tickerView.hideCloseButton()
            }
        }

        tickerView.buildView()
        tickerView.visibility = View.VISIBLE
    }

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
