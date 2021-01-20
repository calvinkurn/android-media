package com.tokopedia.flight.orderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.orderdetail.presentation.adapter.FlightOrderDetailSimpleAdapter
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailPassengerModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailSimpleModel
import com.tokopedia.flight.orderdetail.presentation.utils.OrderDetailUtils
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_flight_order_detail_web_checkin.view.*

/**
 * @author by furqan on 13/11/2020
 */
class FlightOrderDetailWebCheckInViewHolder(view: View,
                                            private val listener: Listener)
    : RecyclerView.ViewHolder(view) {

    fun bind(element: FlightOrderDetailJourneyModel,
             passengers: List<FlightOrderDetailPassengerModel>,
             isDeparture: Boolean) {
        with(itemView) {
            renderCheckInStatus(element)

            tgFlightOrderWebCheckInPassengerDetail.text = context.getString(R.string.flight_order_detail_passenger_detail_title).toUpperCase()

            if (isDeparture) {
                tgFlightOrderWebCheckInTitle.text = context.getString(R.string.flight_order_detail_departure_ticket_title)
            } else {
                tgFlightOrderWebCheckInTitle.text = context.getString(R.string.flight_order_detail_return_ticket_title)
            }

            if (element.airlineLogo != null) {
                ivFlightOrderDepartureAirlineLogo.loadImage(element.airlineLogo!!)
            } else {
                ivFlightOrderDepartureAirlineLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.flight_ic_multi_airlines))
            }

            tgFlightOrderDepartureAirlineName.text = element.airlineName

            tgFlightOrderDepartureJourneyTrip.text = context.getString(R.string.flight_order_detail_trip_city,
                    element.departureCityName, element.departureId,
                    element.arrivalCityName, element.arrivalId)

            if (element.routes.isNotEmpty() && element.routes[0].departureTerminal.isNotEmpty()) {
                tgFlightOrderDepartureAirport.text = context.getString(R.string.flight_order_detail_airport_with_terminal,
                        element.departureAirportName, element.routes[0].departureTerminal)
            } else {
                tgFlightOrderDepartureAirport.text = element.departureAirportName
            }

            if (element.totalTransit > 0) {
                tgFlightOrderDepartureDetail.text = context.getString(R.string.flight_order_detail_airport_journey_with_transit,
                        element.departureDateAndTime.first, element.departureDateAndTime.second, element.totalTransit)
            } else {
                tgFlightOrderDepartureDetail.text = context.getString(R.string.flight_order_detail_airport_journey_without_transit,
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
        with(itemView) {
            rvFlightOrderWebCheckInPassengerDetail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvFlightOrderWebCheckInPassengerDetail.setHasFixedSize(true)
            rvFlightOrderWebCheckInPassengerDetail.adapter = adapter
        }
    }

    private fun renderCheckInStatus(element: FlightOrderDetailJourneyModel) {
        with(itemView) {
            if (element.webCheckIn.webUrl.isNotEmpty()) {
                val checkInOpenDate = FlightDateUtil.stringToDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, element.webCheckIn.startTime)
                val checkInCloseDate = FlightDateUtil.stringToDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, element.webCheckIn.endTime)
                val currentTime = FlightDateUtil.getCurrentDate()

                when {
                    currentTime.before(checkInOpenDate) -> {
                        tgFlightOrderWebCheckInStatus.text = context.getString(R.string.flight_order_detail_web_checkin_not_open)
                        setupCheckInStatusGrey()
                        viewDisabled()
                    }
                    currentTime.after(checkInCloseDate) -> {
                        tgFlightOrderWebCheckInStatus.text = context.getString(R.string.flight_order_detail_web_checkin_closed)
                        setupCheckInStatusGrey()
                        viewDisabled()
                    }
                    else -> {
                        tgFlightOrderWebCheckInStatus.text = context.getString(R.string.flight_order_detail_web_checkin_available)
                        setupCheckInStatusBlue()
                        viewEnabled()
                    }
                }
            } else {
                tgFlightOrderWebCheckInStatus.text = context.getString(R.string.flight_order_detail_web_checkin_not_available)
                setupCheckInStatusGrey()
                viewDisabled()
            }
        }
    }

    private fun setupCheckInStatusBlue() {
        with(itemView) {
            tgFlightOrderWebCheckInStatus.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_B500))
            OrderDetailUtils.changeShapeColor(context, tgFlightOrderWebCheckInStatus.background, com.tokopedia.unifyprinciples.R.color.Unify_B100)
        }
    }

    private fun setupCheckInStatusGrey() {
        with(itemView) {
            tgFlightOrderWebCheckInStatus.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            OrderDetailUtils.changeShapeColor(context, tgFlightOrderWebCheckInStatus.background, com.tokopedia.unifyprinciples.R.color.Unify_N50)
        }
    }

    private fun viewEnabled() {
        with(itemView) {
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
        with(itemView) {
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