package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import kotlinx.android.synthetic.main.item_flight_booking_amenity.view.*

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationChooseReasonViewHolder(itemView: View,
                                               private val listener: Listener)
    : AbstractViewHolder<FlightCancellationPassengerEntity.Reason>(itemView) {

    override fun bind(element: FlightCancellationPassengerEntity.Reason) {
        with(itemView) {
            tv_title.text = element.title
            if (listener.isItemChecked(element)) {
                image_checked.visibility = View.VISIBLE
            } else {
                image_checked.visibility = View.GONE
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