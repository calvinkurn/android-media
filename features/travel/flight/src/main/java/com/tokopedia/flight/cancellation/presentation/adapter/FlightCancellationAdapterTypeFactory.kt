package com.tokopedia.flight.cancellation.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationViewHolder
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel

/**
 * @author by furqan on 14/07/2020
 */
class FlightCancellationAdapterTypeFactory(private val flightCancellationListener: FlightCancellationViewHolder.FlightCancellationListener)
    : BaseAdapterTypeFactory(), FlightCancellationTypeFactory {

    override fun type(cancellationModel: FlightCancellationModel): Int = FlightCancellationViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                FlightCancellationViewHolder.LAYOUT -> FlightCancellationViewHolder(parent, flightCancellationListener)
                else -> super.createViewHolder(parent, type)
            }

}