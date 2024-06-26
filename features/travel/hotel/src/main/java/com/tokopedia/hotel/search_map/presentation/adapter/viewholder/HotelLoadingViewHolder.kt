package com.tokopedia.hotel.search_map.presentation.adapter.viewholder

import android.view.ViewGroup
import android.widget.AbsListView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.PropertyHorizontalSearchShimmeringLoadingBinding
import com.tokopedia.hotel.search_map.data.model.HotelLoadingModel

class HotelLoadingViewHolder(val binding: PropertyHorizontalSearchShimmeringLoadingBinding): AbstractViewHolder<HotelLoadingModel>(binding.root) {
    override fun bind(element: HotelLoadingModel) {
        binding.root.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    companion object {
        val LAYOUT = R.layout.property_horizontal_search_shimmering_loading
    }
}