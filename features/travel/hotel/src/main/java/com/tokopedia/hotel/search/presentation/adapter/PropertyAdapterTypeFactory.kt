package com.tokopedia.hotel.search.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.presentation.adapter.viewholder.SearchPropertyViewHolder

class PropertyAdapterTypeFactory(val callback: BaseEmptyViewHolder.Callback) : BaseAdapterTypeFactory() {

    fun type(data: Property): Int = SearchPropertyViewHolder.LAYOUT

    override fun type(viewModel: LoadingModel): Int = R.layout.property_search_shimmer_loading

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SearchPropertyViewHolder.LAYOUT -> SearchPropertyViewHolder(parent)
            R.layout.property_search_shimmer_loading -> LoadingViewholder(parent)
            EmptyViewHolder.LAYOUT -> EmptyViewHolder(parent, callback)
            else -> super.createViewHolder(parent, type)
        }
    }
}