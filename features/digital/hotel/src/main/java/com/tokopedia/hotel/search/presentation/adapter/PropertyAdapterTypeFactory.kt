package com.tokopedia.hotel.search.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.*
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.presentation.adapter.viewholder.SearchPropertyViewHolder

class PropertyAdapterTypeFactory(val callback: BaseEmptyViewHolder.Callback): BaseAdapterTypeFactory() {

    fun type(data: Property): Int = SearchPropertyViewHolder.LAYOUT

    override fun type(viewModel: LoadingModel): Int = R.layout.property_search_shimmer_loading

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            SearchPropertyViewHolder.LAYOUT -> SearchPropertyViewHolder(parent)
            R.layout.property_search_shimmer_loading -> LoadingViewholder(parent)
            emptyLayout -> return EmptyViewHolder(parent, callback)
            errorLayout -> return ErrorNetworkViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModel: EmptyModel): Int {
        return emptyLayout
    }

    override fun type(viewModel: ErrorNetworkModel): Int {
        return errorLayout
    }
    companion object {
        val emptyLayout: Int = R.layout.item_hotel_room_empty_list
        val errorLayout: Int = R.layout.item_network_error_view
    }
}