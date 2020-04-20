package com.tokopedia.common.travel.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.travel.R

/**
 * @author by alvarisi on 12/22/17.
 */
class TravelSearchShimmeringViewHolder(itemView: View) : AbstractViewHolder<LoadingModel>(itemView) {
    private val linearLayout: LinearLayout = itemView.findViewById(R.id.container)

    override fun bind(flightSearchViewModel: LoadingModel) {
        val inflater = itemView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        linearLayout.removeAllViews()
        renderLoadingItem(inflater)
    }

    private fun renderLoadingItem(inflater: LayoutInflater) {
        for (i in 1 until DEFAULT_MAX_ROW) {
            val newPartialView = inflater.inflate(R.layout.partial_travel_search_shimmering_loading, null, false)
            linearLayout.addView(newPartialView)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_travel_search_shimmering
        private const val DEFAULT_MAX_ROW = 10
    }

}