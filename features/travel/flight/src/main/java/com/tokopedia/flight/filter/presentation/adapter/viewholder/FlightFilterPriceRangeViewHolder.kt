package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.model.PriceRangeModel

/**
 * @author by furqan on 20/02/2020
 */
class FlightFilterPriceRangeViewHolder(val view: View): AbstractViewHolder<PriceRangeModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flight_filter_price_range
    }

    override fun bind(element: PriceRangeModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}