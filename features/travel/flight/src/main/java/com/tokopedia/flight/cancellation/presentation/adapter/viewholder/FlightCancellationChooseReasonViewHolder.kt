package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.databinding.ItemFlightBookingAmenityBinding

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationChooseReasonViewHolder(val binding: ItemFlightBookingAmenityBinding,
                                               private val listener: Listener)
    : AbstractViewHolder<FlightCancellationPassengerEntity.Reason>(binding.root) {

    override fun bind(element: FlightCancellationPassengerEntity.Reason) {
        with(binding) {
            tvTitle.text = element.title
            if (listener.isItemChecked(element)) {
                imageChecked.visibility = View.VISIBLE
            } else {
                imageChecked.visibility = View.GONE
            }
        }
    }

    interface Listener {
        fun isItemChecked(reason: FlightCancellationPassengerEntity.Reason): Boolean
    }

    companion object {
        val LAYOUT = R.layout.item_flight_booking_amenity
    }
}