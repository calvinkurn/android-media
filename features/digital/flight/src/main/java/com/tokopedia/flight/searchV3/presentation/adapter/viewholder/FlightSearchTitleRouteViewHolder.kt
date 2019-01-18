package com.tokopedia.flight.searchV3.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.searchV3.presentation.model.FlightSearchTitleRouteViewModel

/**
 * @author by furqan on 18/01/19
 */
class FlightSearchTitleRouteViewHolder(itemView: View?):
        AbstractViewHolder<FlightSearchTitleRouteViewModel>(itemView) {



    constructor(itemView: View?) {

    }

    override fun bind(element: FlightSearchTitleRouteViewModel?) {
        .text = getString(element!!.titleRes)
    }

    companion object {
        val LAYOUT = R.layout.item_flight_search_selengkapnya
    }

}