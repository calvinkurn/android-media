package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationReviewPassengerAdapter
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.flight.common.view.FullDividerItemDecoration
import com.tokopedia.flight.databinding.ItemFlightCancellationReviewBinding
import com.tokopedia.utils.date.DateUtil

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationReviewViewHolder(val binding: ItemFlightCancellationReviewBinding) :
        AbstractViewHolder<FlightCancellationModel>(binding.root) {

    private lateinit var adapter: FlightCancellationReviewPassengerAdapter

    override fun bind(element: FlightCancellationModel) {
        with(binding) {
            adapter = FlightCancellationReviewPassengerAdapter()
            recyclerViewPassenger.layoutManager = LinearLayoutManager(itemView.context)
            recyclerViewPassenger.adapter = adapter

            val departureCityAirportCode = if (element.flightCancellationJourney.departureCityCode.isEmpty())
                element.flightCancellationJourney.departureAirportId
            else element.flightCancellationJourney.departureCityCode
            val arrivalCityAirportCode = if (element.flightCancellationJourney.arrivalCityCode.isEmpty())
                element.flightCancellationJourney.arrivalAirportId
            else element.flightCancellationJourney.arrivalCityCode
            val departureDate = DateUtil.formatDate(
                    DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    DateUtil.FORMAT_DATE,
                    element.flightCancellationJourney.departureTime)
            val departureTime = DateUtil.formatDate(
                    DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    DateUtil.HH_MM,
                    element.flightCancellationJourney.departureTime)
            val arrivalTime = DateUtil.formatDate(
                    DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    DateUtil.HH_MM,
                    element.flightCancellationJourney.arrivalTime)

            tvDepartureLabel.text = itemView.context.getString(R.string.flight_cancellation_review_journey_number, adapterPosition + 1)
            tvDepartureTimeLabel.text = departureDate
            tvJourneyDetailLabel.text = "${element.flightCancellationJourney.departureCity} ($departureCityAirportCode) - ${element.flightCancellationJourney.arrivalCity} ($arrivalCityAirportCode)"
            airlineName.text = String.format(getString(R.string.flight_booking_trip_info_airport_format), departureTime, arrivalTime)

            adapter.addData(element.passengerModelList)

            if (element.passengerModelList.size < 2) {
                removePassengerRecyclerDivider()
            } else {
                addPassengerRecyclerDivider()
            }
        }
    }

    private fun removePassengerRecyclerDivider() {
        with(binding) {
            recyclerViewPassenger.clearItemDecoration()
        }
    }

    private fun addPassengerRecyclerDivider() {
        with(binding) {
            recyclerViewPassenger.addItemDecoration(FullDividerItemDecoration(itemView.context))
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation_review
    }
}