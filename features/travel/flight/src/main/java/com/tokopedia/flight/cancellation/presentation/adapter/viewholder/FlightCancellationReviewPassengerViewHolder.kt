package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger
import kotlinx.android.synthetic.main.item_flight_review_cancellation_passenger.view.*

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationReviewPassengerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(passenger: FlightCancellationPassengerModel) {
        with(itemView) {
            tv_passenger_name.text = "${passenger.titleString} ${passenger.firstName} ${passenger.lastName}"
            when (passenger.type) {
                FlightBookingPassenger.ADULT -> tv_passenger_type.setText(R.string.flightbooking_price_adult_label)
                FlightBookingPassenger.CHILDREN -> tv_passenger_type.setText(R.string.flightbooking_price_child_label)
                FlightBookingPassenger.INFANT -> tv_passenger_type.setText(R.string.flightbooking_price_infant_label)
                else -> tv_passenger_type.setText(R.string.flightbooking_price_adult_label)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_review_cancellation_passenger
    }
}