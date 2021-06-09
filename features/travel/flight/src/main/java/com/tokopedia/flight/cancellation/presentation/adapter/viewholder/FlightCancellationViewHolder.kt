package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationPassengerAdapter
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.common.util.FlightDateUtil

/**
 * @author by furqan on 14/07/2020
 */
class FlightCancellationViewHolder(itemView: View,
                                   val listener: FlightCancellationListener)
    : AbstractViewHolder<FlightCancellationModel>(itemView),
        FlightCancellationViewHolderListener {

    private var tvDepartureDetail: TextView = itemView.findViewById(R.id.tv_departure_time_label)
    private var tvJourneyDetail: TextView = itemView.findViewById(R.id.tv_journey_detail_label)
    private var tvAirlineName: TextView = itemView.findViewById(R.id.airline_name)
    private var tvDuration: TextView = itemView.findViewById(R.id.duration)
    private var checkBoxJourney: CheckBox = itemView.findViewById(R.id.checkbox)
    private var verticalRecyclerView: VerticalRecyclerView = itemView.findViewById(R.id.recycler_view_passenger)

    private lateinit var flightCancellationPassengerAdapter: FlightCancellationPassengerAdapter
    private var isJourneyChecked = false
    private var uncheckAllData = true

    override fun bind(element: FlightCancellationModel) {
        flightCancellationPassengerAdapter = FlightCancellationPassengerAdapter(listener, this, adapterPosition)
        verticalRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
        verticalRecyclerView.adapter = flightCancellationPassengerAdapter

        val departureCityAirportCode: String = if (element.flightCancellationJourney.departureCityCode == null ||
                element.flightCancellationJourney.departureCityCode.isEmpty()) element.flightCancellationJourney.departureAirportId
        else element.flightCancellationJourney.departureCityCode
        val arrivalCityAirportCode: String = if (element.flightCancellationJourney.arrivalCityCode == null ||
                element.flightCancellationJourney.arrivalCityCode.isEmpty()) element.flightCancellationJourney.arrivalAirportId
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

        tvDepartureDetail.text = itemView.context.getString(R.string.flight_cancellation_journey_title,
                adapterPosition + 1, departureDate)
        tvJourneyDetail.text = String.format("%s (%s) - %s (%s)",
                element.flightCancellationJourney.departureCity,
                departureCityAirportCode,
                element.flightCancellationJourney.arrivalCity,
                arrivalCityAirportCode)
        tvAirlineName.text = element.flightCancellationJourney.airlineName
        tvDuration.text = itemView.context.getString(R.string.flight_booking_trip_info_airport_format,
                departureTime, arrivalTime)

        flightCancellationPassengerAdapter.addData(element.passengerModelList)

        checkBoxJourney.setOnCheckedChangeListener { compoundButton, isChecked ->
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

    override fun setJourneyCheck(isJourneyChecked: Boolean) {
        this.isJourneyChecked = isJourneyChecked
    }

    override fun toggleCheckJourney(uncheckAllData: Boolean) {
        this.uncheckAllData = uncheckAllData
        checkBoxJourney.isChecked = isJourneyChecked
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