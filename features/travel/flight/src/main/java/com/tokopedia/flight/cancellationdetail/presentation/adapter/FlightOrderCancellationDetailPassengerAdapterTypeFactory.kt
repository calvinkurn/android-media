package com.tokopedia.flight.cancellationdetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder.FlightOrderCancellationDetailPassengerViewHolder
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationDetailPassengerModel

/**
 * @author by furqan on 08/01/2021
 */
class FlightOrderCancellationDetailPassengerAdapterTypeFactory
    : BaseAdapterTypeFactory(), FlightOrderCancellationDetailPassengerTypeFactory {

    override fun type(model: FlightOrderCancellationDetailPassengerModel): Int =
            FlightOrderCancellationDetailPassengerViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                FlightOrderCancellationDetailPassengerViewHolder.LAYOUT -> FlightOrderCancellationDetailPassengerViewHolder(parent)
                else -> super.createViewHolder(parent, type)
            }
}