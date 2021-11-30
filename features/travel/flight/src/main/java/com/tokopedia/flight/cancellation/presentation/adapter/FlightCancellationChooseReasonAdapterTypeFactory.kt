package com.tokopedia.flight.cancellation.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellation.presentation.adapter.viewholder.FlightCancellationChooseReasonViewHolder
import com.tokopedia.flight.databinding.ItemFlightBookingAmenityBinding

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationChooseReasonAdapterTypeFactory(private val listener: FlightCancellationChooseReasonViewHolder.Listener)
    : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                FlightCancellationChooseReasonViewHolder.LAYOUT -> {
                    val binding = ItemFlightBookingAmenityBinding.bind(parent)
                    FlightCancellationChooseReasonViewHolder(binding, listener)
                }
                else -> super.createViewHolder(parent, type)
            }

    fun type(viewModel: FlightCancellationPassengerEntity.Reason): Int = FlightCancellationChooseReasonViewHolder.LAYOUT

}