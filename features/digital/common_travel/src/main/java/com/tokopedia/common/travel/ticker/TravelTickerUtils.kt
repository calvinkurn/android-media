package com.tokopedia.common.travel.ticker

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel
import com.tokopedia.design.component.ticker.TickerView
import java.util.*

/**
 * @author by furqan on 25/02/19
 */
object TravelTickerUtils {
    private val ANNOUNCEMENT_TYPE = 1
    private val DANGER_TYPE = 2

    fun buildTravelTicker(context: Context?, travelTickerViewModel: TravelTickerViewModel, tickerView: TickerView) {
        val messages = ArrayList<String>()
        messages.add(travelTickerViewModel.message)
        tickerView.setListMessage(messages)
        if (context != null) {
            if (travelTickerViewModel.type == ANNOUNCEMENT_TYPE) {
                tickerView.setHighLightColor(ContextCompat.getColor(context, R.color.tkpd_main_green))
                tickerView.setPageIndicatorOnColor(ContextCompat.getColor(context, R.color.light_green))
                tickerView.setPageIndicatorOffColor(ContextCompat.getColor(context, R.color.light_green))
            } else if (travelTickerViewModel.type == DANGER_TYPE) {
                tickerView.setHighLightColor(ContextCompat.getColor(context, R.color.snackbar_border_error))
                tickerView.setPageIndicatorOnColor(ContextCompat.getColor(context, R.color.colorPink))
                tickerView.setPageIndicatorOffColor(ContextCompat.getColor(context, R.color.colorPink))
                tickerView.hideCloseButton()
            }
        }

        tickerView.buildView()
        tickerView.visibility = View.VISIBLE
    }
}
