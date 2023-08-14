package com.tokopedia.flight.orderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.R
import com.tokopedia.flight.databinding.ItemFlightOrderDetailWebCheckinBinding
import com.tokopedia.flight.orderdetail.presentation.adapter.FlightOrderDetailSimpleAdapter
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailPassengerModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailSimpleModel
import com.tokopedia.flight.orderdetail.presentation.utils.OrderDetailUtils
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate

/**
 * @author by furqan on 13/11/2020
 */
class FlightOrderDetailWebCheckInViewHolder(val binding: ItemFlightOrderDetailWebCheckinBinding,
                                            private val listener: Listener)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(element: FlightOrderDetailJourneyModel,
             passengers: List<FlightOrderDetailPassengerModel>,
             isDeparture: Boolean) {
        with(binding) {
            renderCheckInStatus(element)

            tgFlightOrderWebCheckInPassengerDetail.text = itemView.context.getString(R.string.flight_order_detail_passenger_detail_title).toUpperCase()

            if (isDeparture) {
                tgFlightOrderWebCheckInTitle.text = itemView.context.getString(R.string.flight_order_detail_departure_ticket_title)
            } else {
                tgFlightOrderWebCheckInTitle.text = itemView.context.getString(R.string.flight_order_detail_return_ticket_title)
            }

            if (element.airlineLogo != null) {
                ivFlightOrderDepartureAirlineLogo.loadImage(element.airlineLogo!!)
            } else {
                ivFlightOrderDepartureAirlineLogo.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.flight_ic_multi_airlines))
            }

            tgFlightOrderDepartureAirlineName.text = element.airlineName

            tgFlightOrderDepartureJourneyTrip.text = itemView.context.getString(R.string.flight_order_detail_trip_city,
                    element.departureCityName, element.departureId,
                    element.arrivalCityName, element.arrivalId)

            if (element.routes.isNotEmpty() && element.routes[0].departureTerminal.isNotEmpty()) {
                tgFlightOrderDepartureAirport.text = itemView.context.getString(R.string.flight_order_detail_airport_with_terminal,
                        element.departureAirportName, element.routes[0].departureTerminal)
            } else {
                tgFlightOrderDepartureAirport.text = element.departureAirportName
            }

            if (element.totalTransit > 0) {
                tgFlightOrderDepartureDetail.text = itemView.context.getString(R.string.flight_order_detail_airport_journey_with_transit,
                        element.departureDateAndTime.first, element.departureDateAndTime.second, element.totalTransit)
            } else {
                tgFlightOrderDepartureDetail.text = itemView.context.getString(R.string.flight_order_detail_airport_journey_without_transit,
                        element.departureDateAndTime.first, element.departureDateAndTime.second)
            }

            if (element.routes.isNotEmpty() && element.routes[0].pnr.isNotEmpty()) {
                tgFlightOrderDepartureBookingCode.visibility = View.VISIBLE
                tgFlightOrderDepartureBookingCodeLabel.visibility = View.VISIBLE
                ivFlightOrderDepartureBookingCodeCopy.visibility = View.VISIBLE

                tgFlightOrderDepartureBookingCode.text = element.routes[0].pnr
                ivFlightOrderDepartureBookingCodeCopy.setOnClickListener {

                }
            } else {
                tgFlightOrderDepartureBookingCode.visibility = View.GONE
                tgFlightOrderDepartureBookingCodeLabel.visibility = View.GONE
                ivFlightOrderDepartureBookingCodeCopy.visibility = View.GONE
            }

            renderPassenger(passengers)

            btnFlightOrderDetailWebCheckIn.setOnClickListener {
                listener.onCheckInClicked(element, isDeparture)
            }
        }
    }

    private fun renderPassenger(passengerDataList: List<FlightOrderDetailPassengerModel>) {
        val passengerList = arrayListOf<FlightOrderDetailSimpleModel>()
        for ((index, passenger) in passengerDataList.withIndex()) {
            passengerList.add(FlightOrderDetailSimpleModel(
                    "${index + 1}. ${passenger.titleString} ${passenger.firstName} ${passenger.lastName}",
                    "",
                    false,
                    false,
                    false,
                    false,
                    false
            ))
        }

        val adapter = FlightOrderDetailSimpleAdapter(passengerList)
        with(binding) {
            rvFlightOrderWebCheckInPassengerDetail.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
            rvFlightOrderWebCheckInPassengerDetail.setHasFixedSize(true)
            rvFlightOrderWebCheckInPassengerDetail.adapter = adapter
        }
    }

    private fun renderCheckInStatus(element: FlightOrderDetailJourneyModel) {
        with(binding) {
            if (element.webCheckIn.webUrl.isNotEmpty()) {
                val checkInOpenDate = element.webCheckIn.startTime.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)
                val checkInCloseDate = element.webCheckIn.endTime.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)
                val currentTime = DateUtil.getCurrentDate()

                when {
                    currentTime.before(checkInOpenDate) -> {
                        tgFlightOrderWebCheckInStatus.text = itemView.context.getString(R.string.flight_order_detail_web_checkin_not_open)
                        setupCheckInStatusGrey()
                        viewDisabled()
                    }
                    currentTime.after(checkInCloseDate) -> {
                        tgFlightOrderWebCheckInStatus.text = itemView.context.getString(R.string.flight_order_detail_web_checkin_closed)
                        setupCheckInStatusGrey()
                        viewDisabled()
                    }
                    else -> {
                        tgFlightOrderWebCheckInStatus.text = itemView.context.getString(R.string.flight_order_detail_web_checkin_available)
                        setupCheckInStatusBlue()
                        viewEnabled()
                    }
                }
            } else {
                tgFlightOrderWebCheckInStatus.text = itemView.context.getString(R.string.flight_order_detail_web_checkin_not_available)
                setupCheckInStatusGrey()
                viewDisabled()
            }
        }
    }

    private fun setupCheckInStatusBlue() {
        with(binding) {
            tgFlightOrderWebCheckInStatus.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_BN500))
            OrderDetailUtils.changeShapeColor(itemView.context, tgFlightOrderWebCheckInStatus.background, com.tokopedia.unifyprinciples.R.color.Unify_BN50)
        }
    }

    private fun setupCheckInStatusGrey() {
        with(binding) {
            tgFlightOrderWebCheckInStatus.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
            OrderDetailUtils.changeShapeColor(itemView.context, tgFlightOrderWebCheckInStatus.background, com.tokopedia.unifyprinciples.R.color.Unify_NN50)
        }
    }

    private fun viewEnabled() {
        with(binding) {
            cardContainerTicketDetails.isEnabled = true
            ticketTopView.isEnabled = true
            titleFlightOrderDepartureTicket.isEnabled = true
            tgFlightOrderWebCheckInTitle.isEnabled = true
            containerFlightOrderDepartureTicket.isEnabled = true
            ivFlightOrderDepartureAirlineLogo.isEnabled = true
            tgFlightOrderDepartureAirlineName.isEnabled = true
            tgFlightOrderDepartureJourneyTrip.isEnabled = true
            tgFlightOrderDepartureAirport.isEnabled = true
            tgFlightOrderDepartureDetail.isEnabled = true
            tgFlightOrderDepartureBookingCodeLabel.isEnabled = true
            tgFlightOrderDepartureBookingCode.isEnabled = true
            ivFlightOrderDepartureBookingCodeCopy.isEnabled = true
            tgFlightOrderWebCheckInPassengerDetail.isEnabled = true
            rvFlightOrderWebCheckInPassengerDetail.isEnabled = true
            btnFlightOrderDetailWebCheckIn.isEnabled = true
        }
    }

    private fun viewDisabled() {
        with(binding) {
            cardContainerTicketDetails.isEnabled = false
            ticketTopView.isEnabled = false
            titleFlightOrderDepartureTicket.isEnabled = false
            tgFlightOrderWebCheckInTitle.isEnabled = false
            containerFlightOrderDepartureTicket.isEnabled = false
            ivFlightOrderDepartureAirlineLogo.isEnabled = false
            tgFlightOrderDepartureAirlineName.isEnabled = false
            tgFlightOrderDepartureJourneyTrip.isEnabled = false
            tgFlightOrderDepartureAirport.isEnabled = false
            tgFlightOrderDepartureDetail.isEnabled = false
            tgFlightOrderDepartureBookingCodeLabel.isEnabled = false
            tgFlightOrderDepartureBookingCode.isEnabled = false
            ivFlightOrderDepartureBookingCodeCopy.isEnabled = false
            tgFlightOrderWebCheckInPassengerDetail.isEnabled = false
            rvFlightOrderWebCheckInPassengerDetail.isEnabled = false
            btnFlightOrderDetailWebCheckIn.isEnabled = false
        }
    }


    interface Listener {
        fun onCheckInClicked(journey: FlightOrderDetailJourneyModel, isDeparture: Boolean)
    }

    companion object {
        val LAYOUT = R.layout.item_flight_order_detail_web_checkin
    }
}