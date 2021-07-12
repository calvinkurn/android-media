package com.tokopedia.flight.common.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.flight.common.view.model.EmptyResultModel
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder

/**
 * @author by furqan on 19/05/2020
 */
abstract class FlightAdapterTypeFactory : BaseAdapterTypeFactory(),
        AdapterTypeFactory, ErrorNetworkViewHolder.OnRetryListener {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                EmptyResultViewHolder.LAYOUT -> EmptyResultViewHolder(parent)
                else -> super.createViewHolder(parent, type)
            }

    open fun type(viewModel: EmptyResultModel): Int {
        return EmptyResultViewHolder.LAYOUT
    }

}