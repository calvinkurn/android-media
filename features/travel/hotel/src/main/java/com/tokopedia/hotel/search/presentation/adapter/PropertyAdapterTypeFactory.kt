package com.tokopedia.hotel.search.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.*
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.presentation.adapter.viewholder.SearchPropertyViewHolder
import com.tokopedia.hotel.search_map.data.HotelLoadingModel
import com.tokopedia.hotel.search_map.presentation.adapter.viewholder.HotelLoadingViewHolder
import com.tokopedia.hotel.search_map.presentation.adapter.viewholder.HotelSearchMapItemViewHolder

class PropertyAdapterTypeFactory(val callback: BaseEmptyViewHolder.Callback) : BaseAdapterTypeFactory() {

    fun type(data: Property): Int {
        return if (data.isForHorizontalItem) HotelSearchMapItemViewHolder.LAYOUT
        else SearchPropertyViewHolder.LAYOUT
    }

    override fun type(viewModel: ErrorNetworkModel): Int = ErrorNetworkViewHolder.LAYOUT

    override fun type(viewModel: LoadingModel): Int = R.layout.property_search_shimmer_loading

    fun type(viewModel: HotelLoadingModel): Int = HotelLoadingViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SearchPropertyViewHolder.LAYOUT -> SearchPropertyViewHolder(parent)
            ErrorNetworkViewHolder.LAYOUT -> ErrorNetworkViewHolder(parent)
            R.layout.property_search_shimmer_loading -> LoadingViewholder(parent)
            EmptyViewHolder.LAYOUT -> EmptyViewHolder(parent, callback)
            HotelSearchMapItemViewHolder.LAYOUT -> HotelSearchMapItemViewHolder(parent)
            HotelLoadingViewHolder.LAYOUT -> HotelLoadingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}