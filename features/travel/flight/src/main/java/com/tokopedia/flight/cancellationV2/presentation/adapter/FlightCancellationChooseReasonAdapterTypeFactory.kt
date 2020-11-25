package com.tokopedia.flight.cancellationV2.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.cancellationV2.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellationV2.presentation.adapter.viewholder.FlightCancellationChooseReasonViewHolder

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationChooseReasonAdapterTypeFactory(private val listener: FlightCancellationChooseReasonViewHolder.Listener)
    : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                FlightCancellationChooseReasonViewHolder.LAYOUT -> FlightCancellationChooseReasonViewHolder(parent, listener)
                else -> super.createViewHolder(parent, type)
            }

    fun type(viewModel: FlightCancellationPassengerEntity.Reason): Int = FlightCancellationChooseReasonViewHolder.LAYOUT

}