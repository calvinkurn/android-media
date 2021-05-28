package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationReviewPassengerAdapter
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.view.FullDividerItemDecoration
import kotlinx.android.synthetic.main.item_flight_cancellation_review.view.*

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationReviewViewHolder(itemView: View) :
        AbstractViewHolder<FlightCancellationModel>(itemView) {

    private lateinit var adapter: FlightCancellationReviewPassengerAdapter

    override fun bind(element: FlightCancellationModel) {
        with(itemView) {
            adapter = FlightCancellationReviewPassengerAdapter()
            recycler_view_passenger.layoutManager = LinearLayoutManager(context)
            recycler_view_passenger.adapter = adapter

            val departureCityAirportCode = if (element.flightCancellationJourney.departureCityCode.isEmpty())
                element.flightCancellationJourney.departureAirportId
            else element.flightCancellationJourney.departureCityCode
            val arrivalCityAirportCode = if (element.flightCancellationJourney.arrivalCityCode.isEmpty())
                element.flightCancellationJourney.arrivalAirportId
            else element.flightCancellationJourney.arrivalCityCode
            val departureDate = FlightDateUtil.formatDate(
                    FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    FlightDateUtil.FORMAT_DATE,
                    element.flightCancellationJourney.departureTime)
            val departureTime = FlightDateUtil.formatDate(
                    FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    FlightDateUtil.FORMAT_TIME_DETAIL,
                    element.flightCancellationJourney.departureTime)
            val arrivalTime = FlightDateUtil.formatDate(
                    FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    FlightDateUtil.FORMAT_TIME_DETAIL,
                    element.flightCancellationJourney.arrivalTime)

            tv_departure_label.text = context.getString(R.string.flight_cancellation_review_journey_number, adapterPosition + 1)
            tv_departure_time_label.text = departureDate
            tv_journey_detail_label.text = "${element.flightCancellationJourney.departureCity} ($departureCityAirportCode) - ${element.flightCancellationJourney.arrivalCity} ($arrivalCityAirportCode)"
            airline_name.text = context.getString(R.string.flight_booking_trip_info_airport_format, departureTime, arrivalTime)

            adapter.addData(element.passengerModelList)

            if (element.passengerModelList.size < 2) {
                removePassengerRecyclerDivider()
            } else {
                addPassengerRecyclerDivider()
            }
        }
    }

    private fun removePassengerRecyclerDivider() {
        with(itemView) {
            recycler_view_passenger.clearItemDecoration()
        }
    }

    private fun addPassengerRecyclerDivider() {
        with(itemView) {
            recycler_view_passenger.addItemDecoration(FullDividerItemDecoration(context))
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation_review
    }
}