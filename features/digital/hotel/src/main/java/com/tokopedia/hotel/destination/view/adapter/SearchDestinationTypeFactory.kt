package com.tokopedia.hotel.destination.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.hotel.destination.view.adapter.viewholder.HotelDestinationEmptyResultViewHolder
import com.tokopedia.hotel.destination.view.adapter.viewholder.HotelDestinationShimmeringViewHolder
import com.tokopedia.hotel.destination.view.adapter.viewholder.SearchDestinationViewHolder

/**
 * @author by jessica on 25/03/19
 */

class SearchDestinationTypeFactory(val searchDestinationListener: SearchDestinationListener): BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            HotelDestinationShimmeringViewHolder.LAYOUT -> return HotelDestinationShimmeringViewHolder(parent)
            SearchDestinationViewHolder.LAYOUT -> return SearchDestinationViewHolder(parent, searchDestinationListener)
            HotelDestinationEmptyResultViewHolder.LAYOUT -> return HotelDestinationEmptyResultViewHolder(parent)
            errorLayout -> return ErrorNetworkViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

    fun type(searchDestination: SearchDestination): Int {
        return SearchDestinationViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return HotelDestinationShimmeringViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel): Int {
        return HotelDestinationEmptyResultViewHolder.LAYOUT
    }

    override fun type(viewModel: ErrorNetworkModel): Int {
        return errorLayout
    }

    companion object {
        val errorLayout: Int = R.layout.item_network_error_view
    }
}