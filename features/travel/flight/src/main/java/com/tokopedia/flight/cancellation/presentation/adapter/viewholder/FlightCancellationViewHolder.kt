package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationPassengerAdapter
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.databinding.ItemFlightCancellationBinding
import com.tokopedia.utils.date.DateUtil

/**
 * @author by furqan on 14/07/2020
 */
class FlightCancellationViewHolder(val binding: ItemFlightCancellationBinding,
                                   val listener: FlightCancellationListener)
    : AbstractViewHolder<FlightCancellationModel>(binding.root),
        FlightCancellationViewHolderListener {

    private lateinit var flightCancellationPassengerAdapter: FlightCancellationPassengerAdapter
    private var isJourneyChecked = false
    private var uncheckAllData = true

    override fun bind(element: FlightCancellationModel) {
            flightCancellationPassengerAdapter =
                FlightCancellationPassengerAdapter(listener, this, adapterPosition)
            binding.recyclerViewPassenger.layoutManager = LinearLayoutManager(itemView.context)
            binding.recyclerViewPassenger.adapter = flightCancellationPassengerAdapter
        with(binding){
            val departureCityAirportCode: String =
                if (element.flightCancellationJourney.departureCityCode == null ||
                    element.flightCancellationJourney.departureCityCode.isEmpty()
                ) element.flightCancellationJourney.departureAirportId
                else element.flightCancellationJourney.departureCityCode
            val arrivalCityAirportCode: String =
                if (element.flightCancellationJourney.arrivalCityCode == null ||
                    element.flightCancellationJourney.arrivalCityCode.isEmpty()
                ) element.flightCancellationJourney.arrivalAirportId
                else element.flightCancellationJourney.arrivalCityCode
            val departureDate = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.FORMAT_DATE,
                element.flightCancellationJourney.departureTime
            )
            val departureTime = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.HH_MM,
                element.flightCancellationJourney.departureTime
            )
            val arrivalTime = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.HH_MM,
                element.flightCancellationJourney.arrivalTime
            )

            tvDepartureTimeLabel.text = itemView.context.getString(
                R.string.flight_cancellation_journey_title,
                adapterPosition + 1, departureDate
            )
            tvJourneyDetailLabel.text = String.format(
                "%s (%s) - %s (%s)",
                element.flightCancellationJourney.departureCity,
                departureCityAirportCode,
                element.flightCancellationJourney.arrivalCity,
                arrivalCityAirportCode
            )
            airlineName.text = element.flightCancellationJourney.airlineName
            duration.text = String.format(
                itemView.context.getString(R.string.flight_booking_trip_info_airport_format),
                departureTime, arrivalTime
            )

            flightCancellationPassengerAdapter.addData(element.passengerModelList)

            checkbox.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (compoundButton.isPressed) uncheckAllData = !isChecked
                isJourneyChecked = isChecked

                if (isJourneyChecked) {
                    flightCancellationPassengerAdapter.checkAllData()
                } else if (uncheckAllData) {
                    flightCancellationPassengerAdapter.uncheckAllData()
                }
            }

            itemView.setOnClickListener {
                toggleCheckJourney(true)
            }
        }
    }

    override fun setJourneyCheck(isJourneyChecked: Boolean) {
        this.isJourneyChecked = isJourneyChecked
    }

    override fun toggleCheckJourney(uncheckAllData: Boolean) {
        this.uncheckAllData = uncheckAllData
        binding.checkbox.isChecked = isJourneyChecked
    }

    interface FlightCancellationListener {
        fun onPassengerChecked(passengerModel: FlightCancellationPassengerModel, position: Int)
        fun onPassengerUnchecked(passengerModel: FlightCancellationPassengerModel, position: Int)
        fun isChecked(passengerModel: FlightCancellationPassengerModel): Boolean
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation
    }

}

interface FlightCancellationViewHolderListener {
    fun setJourneyCheck(isJourneyChecked: Boolean)
    fun toggleCheckJourney(uncheckAllData: Boolean)
}