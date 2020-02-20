package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.model.PriceRangeModel
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
            etFlightLowestPrice.setText(element.initialStartValue.toString())
            etFlightHighestPrice.setText(element.initialEndValue.toString())
            rsuFlightFilterPrice.updateStartValue(element.initialStartValue)
            rsuFlightFilterPrice.updateEndValue(element.initialEndValue)
            rsuFlightFilterPrice.setInitialValue(element.selectedStartValue, element.selectedEndValue)
        }
    }
}