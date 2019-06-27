package com.tokopedia.flight.searchV3.presentation.adapter.viewholder

import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.searchV3.presentation.model.FlightSearchTitleRouteViewModel

/**
 * @author by furqan on 18/01/19
 */
class FlightSearchTitleRouteViewHolder(itemView: View?) :
        AbstractViewHolder<FlightSearchTitleRouteViewModel>(itemView) {

    private lateinit var tvTitleRoute: AppCompatTextView

    init {
        if (itemView != null) {
            tvTitleRoute = itemView.findViewById(R.id.tv_flight_search_title_route)
        }
    }

    override fun bind(element: FlightSearchTitleRouteViewModel?) {
        if (element != null) {
            tvTitleRoute.text = getString(element.titleRes)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_search_title_route
    }

}