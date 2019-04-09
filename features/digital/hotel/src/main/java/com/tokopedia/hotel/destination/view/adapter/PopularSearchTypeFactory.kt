package com.tokopedia.hotel.destination.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.view.adapter.viewholder.PopularSearchViewHolder

/**
 * @author by jessica on 25/03/19
 */

class PopularSearchTypeFactory: BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            PopularSearchViewHolder.LAYOUT -> return PopularSearchViewHolder(parent)
            errorLayout -> return ErrorNetworkViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

    fun type(popularSearch: PopularSearch): Int {
        return PopularSearchViewHolder.LAYOUT
    }

    override fun type(viewModel: ErrorNetworkModel): Int {
        return errorLayout
    }

    companion object {
        val errorLayout: Int = R.layout.item_network_error_view
    }
}