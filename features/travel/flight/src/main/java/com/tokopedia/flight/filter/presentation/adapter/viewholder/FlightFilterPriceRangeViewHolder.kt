package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.filter.presentation.model.PriceRangeModel
import com.tokopedia.unifycomponents.RangeSliderUnify
import kotlinx.android.synthetic.main.item_flight_filter_price_range.view.*

/**
 * @author by furqan on 20/02/2020
 */
class FlightFilterPriceRangeViewHolder(val view: View) : AbstractViewHolder<PriceRangeModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flight_filter_price_range
    }

    override fun bind(element: PriceRangeModel) {
        with(view) {

            etFlightLowestPrice.setText(FlightCurrencyFormatUtil.convertToIdrPriceWithoutSymbol(element.initialStartValue))
            etFlightHighestPrice.setText(FlightCurrencyFormatUtil.convertToIdrPriceWithoutSymbol(element.initialEndValue))
            rsuFlightFilterPrice.updateStartValue(element.initialStartValue)
            rsuFlightFilterPrice.updateEndValue(element.initialEndValue)
            rsuFlightFilterPrice.setInitialValue(element.selectedStartValue, element.selectedEndValue)

            rsuFlightFilterPrice.onSliderMoveListener = object : RangeSliderUnify.OnSliderMoveListener {
                override fun onSliderMove(p0: Pair<Int, Int>) {
                    etFlightLowestPrice.setText(FlightCurrencyFormatUtil.convertToIdrPriceWithoutSymbol(p0.first))
                    etFlightHighestPrice.setText(FlightCurrencyFormatUtil.convertToIdrPriceWithoutSymbol(p0.second))
                }

            }
        }
    }
}