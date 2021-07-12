package com.tokopedia.flight.search.presentation.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R

/**
 * @author by alvarisi on 12/22/17.
 */
class FlightSearchShimmeringViewHolder(itemView: View) : AbstractViewHolder<LoadingModel>(itemView) {
    private val linearLayout: LinearLayout = itemView.findViewById(R.id.flightSearchShimmering)

    override fun bind(flightSearchViewModel: LoadingModel) {
        val inflater = itemView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        linearLayout.removeAllViews()
        renderLoadingItem(inflater)
    }

    private fun renderLoadingItem(inflater: LayoutInflater) {
        for (i in 1 until DEFAULT_MAX_ROW) {
            val newPartialView = inflater.inflate(R.layout.partial_flight_search_shimmering_loading, null, false)
            linearLayout.addView(newPartialView)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_search_shimmering
        private const val DEFAULT_MAX_ROW = 10
    }

}