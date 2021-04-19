package com.tokopedia.hotel.search_map.presentation.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search_map.data.HotelLoadingModel

class HotelLoadingViewHolder(itemView: View): AbstractViewHolder<HotelLoadingModel>(itemView) {
    override fun bind(element: HotelLoadingModel) {
        itemView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    companion object {
        val LAYOUT = R.layout.property_horizontal_search_shimmering_loading
    }
}