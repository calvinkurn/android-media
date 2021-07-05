package com.tokopedia.flight.cancellationdetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder.FlightOrderCancellationListViewHolder
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationListModel

/**
 * @author by furqan on 06/01/2021
 */
class FlightOrderCancellationListAdapterTypeFactory : BaseAdapterTypeFactory(),
        FlightOrderCancellationListTypeFactory {

    override fun type(model: FlightOrderCancellationListModel): Int =
            FlightOrderCancellationListViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                FlightOrderCancellationListViewHolder.LAYOUT -> FlightOrderCancellationListViewHolder(parent)
                else -> super.createViewHolder(parent, type)
            }

}