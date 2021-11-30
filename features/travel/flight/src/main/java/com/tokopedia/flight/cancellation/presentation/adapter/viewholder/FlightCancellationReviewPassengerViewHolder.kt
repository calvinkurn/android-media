package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.databinding.ItemFlightReviewCancellationPassengerBinding
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationReviewPassengerViewHolder(val binding: ItemFlightReviewCancellationPassengerBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(passenger: FlightCancellationPassengerModel) {
        with(binding) {
            tvPassengerName.text = "${passenger.titleString} ${passenger.firstName} ${passenger.lastName}"
            when (passenger.type) {
                FlightBookingPassenger.ADULT.value -> tvPassengerType.setText(R.string.flightbooking_price_adult_label)
                FlightBookingPassenger.CHILDREN.value -> tvPassengerType.setText(R.string.flightbooking_price_child_label)
                FlightBookingPassenger.INFANT.value -> tvPassengerType.setText(R.string.flightbooking_price_infant_label)
                else -> tvPassengerName.setText(R.string.flightbooking_price_adult_label)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_review_cancellation_passenger
    }
}