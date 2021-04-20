package com.tokopedia.flight.orderdetail.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import com.tokopedia.flight.orderdetail.presentation.utils.OrderDetailUtils
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.view_flight_order_detail_journey.view.*

/**
 * @author by furqan on 10/11/2020
 */
class FlightOrderDetailJourneyView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    var listener: Listener? = null

    private var hasETicket: Boolean = false
    private var journeys: List<FlightOrderDetailJourneyModel> = arrayListOf()
    private var refundableColor: String = ""

    init {
        View.inflate(context, R.layout.view_flight_order_detail_journey, this)
    }

    fun setData(hasETicket: Boolean,
                journeyList: List<FlightOrderDetailJourneyModel>) {
        this.hasETicket = hasETicket
        this.journeys = journeyList
    }

    fun buildView() {
        renderTicketTicker()
        renderEticketButton()
        renderTicketView()
    }

    private fun renderTicketTicker() {
        if (hasETicket) {
            tickerFlightOrderJourneyETicket.visibility = View.VISIBLE
        } else {
            tickerFlightOrderJourneyETicket.visibility = View.GONE
        }
    }

    private fun renderEticketButton() {
        if (hasETicket) {
            btnFlightOrderDetailSendEticket.isEnabled = true
            btnFlightOrderDetailViewEticket.isEnabled = true

            btnFlightOrderDetailSendEticket.setOnClickListener {
                listener?.onSendETicketClicked()
            }
            btnFlightOrderDetailViewEticket.setOnClickListener {
                listener?.onViewETicketClicked()
            }
        } else {
            btnFlightOrderDetailSendEticket.isEnabled = false
            btnFlightOrderDetailViewEticket.isEnabled = false
        }
    }

    private fun renderTicketView() {
        var hasTerminalInfo = false
        if (journeys.isNotEmpty()) {
            val onwardJourney = journeys[0]
            if (onwardJourney.airlineLogo != null) {
                ivFlightOrderDepartureAirlineLogo.loadImage(onwardJourney.airlineLogo!!)
            } else {
                ivFlightOrderDepartureAirlineLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.flight_ic_multi_airlines))
            }

            tgFlightOrderDepartureAirlineName.text = onwardJourney.airlineName

            if (onwardJourney.refundableInfo) {
                tgFlightOrderDepartureTicketRefundableStatus.visibility = View.VISIBLE
                try {
                    refundableColor = "#" + Integer.toHexString(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N50) and  HEX_CODE_TRANSPARENCY)
                    OrderDetailUtils.changeShapeColor(tgFlightOrderDepartureTicketRefundableStatus.background, refundableColor)
                }catch (e: Throwable){
                    e.printStackTrace()
                }
            } else {
                tgFlightOrderDepartureTicketRefundableStatus.visibility = View.GONE
            }

            tgFlightOrderDepartureJourneyTrip.text = context.getString(R.string.flight_order_detail_trip_city,
                    onwardJourney.departureCityName, onwardJourney.departureId,
                    onwardJourney.arrivalCityName, onwardJourney.arrivalId)

            if (onwardJourney.routes.isNotEmpty() && onwardJourney.routes[0].departureTerminal.isNotEmpty()) {
                tgFlightOrderDepartureAirport.text = context.getString(R.string.flight_order_detail_airport_with_terminal,
                        onwardJourney.departureAirportName, onwardJourney.routes[0].departureTerminal)
                hasTerminalInfo = true
            } else {
                tgFlightOrderDepartureAirport.text = onwardJourney.departureAirportName
            }

            if (onwardJourney.totalTransit > 0) {
                tgFlightOrderDepartureDetail.text = context.getString(R.string.flight_order_detail_airport_journey_with_transit,
                        onwardJourney.departureDateAndTime.first, onwardJourney.departureDateAndTime.second, onwardJourney.totalTransit)
            } else {
                tgFlightOrderDepartureDetail.text = context.getString(R.string.flight_order_detail_airport_journey_without_transit,
                        onwardJourney.departureDateAndTime.first, onwardJourney.departureDateAndTime.second)
            }

            if (onwardJourney.routes.isNotEmpty() && onwardJourney.routes[0].pnr.isNotEmpty()) {
                tgFlightOrderDepartureBookingCode.visibility = View.VISIBLE
                tgFlightOrderDepartureBookingCodeLabel.visibility = View.VISIBLE
                ivFlightOrderDepartureBookingCodeCopy.visibility = View.VISIBLE

                tgFlightOrderDepartureBookingCode.text = onwardJourney.routes[0].pnr
                ivFlightOrderDepartureBookingCodeCopy.setOnClickListener {
                    listener?.onPnrCopyClicked(onwardJourney.routes[0].pnr, false)
                }
            } else {
                tgFlightOrderDepartureBookingCode.visibility = View.GONE
                tgFlightOrderDepartureBookingCodeLabel.visibility = View.GONE
                ivFlightOrderDepartureBookingCodeCopy.visibility = View.GONE
            }

            if (journeys.size > 1) {
                renderReturnTicketView(journeys[1], hasTerminalInfo)
            } else {
                renderTerminalNotes(hasTerminalInfo)
                hideReturnTicketView()
            }
        }
    }

    private fun renderReturnTicketView(returnJourney: FlightOrderDetailJourneyModel, hasTerminalInfo: Boolean) {
        var hasTerminal: Boolean = hasTerminalInfo

        titleFlightOrderReturnTicket.visibility = View.VISIBLE
        containerFlightOrderReturnTicket.visibility = View.VISIBLE

        val airlineLogo = returnJourney.airlineLogo
        if (airlineLogo != null) {
            ivFlightOrderReturnAirlineLogo.loadImage(airlineLogo)
        } else {
            ivFlightOrderReturnAirlineLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.flight_ic_multi_airlines))
        }

        tgFlightOrderReturnAirlineName.text = returnJourney.airlineName

        if (returnJourney.refundableInfo) {
            tgFlightOrderReturnTicketRefundableStatus.visibility = View.VISIBLE
        } else {
            tgFlightOrderReturnTicketRefundableStatus.visibility = View.GONE
        }

        tgFlightOrderReturnJourneyTrip.text = context.getString(R.string.flight_order_detail_trip_city,
                returnJourney.departureCityName, returnJourney.departureId,
                returnJourney.arrivalCityName, returnJourney.arrivalId)

        if (returnJourney.routes.isNotEmpty() && returnJourney.routes[0].departureTerminal.isNotEmpty()) {
            tgFlightOrderReturnAirport.text = context.getString(R.string.flight_order_detail_airport_with_terminal,
                    returnJourney.departureAirportName, returnJourney.routes[0].departureTerminal)
            hasTerminal = true
        } else {
            tgFlightOrderReturnAirport.text = returnJourney.departureAirportName
        }

        val departureDateAndTimePair = returnJourney.departureDateAndTime
        if (returnJourney.totalTransit > 0) {
            tgFlightOrderReturnDetail.text = context.getString(R.string.flight_order_detail_airport_journey_with_transit,
                    departureDateAndTimePair.first, departureDateAndTimePair.second, returnJourney.totalTransit)
        } else {
            tgFlightOrderReturnDetail.text = context.getString(R.string.flight_order_detail_airport_journey_without_transit,
                    departureDateAndTimePair.first, departureDateAndTimePair.second)
        }

        if (returnJourney.routes.isNotEmpty() && returnJourney.routes[0].pnr.isNotEmpty()) {
            tgFlightOrderReturnBookingCode.visibility = View.VISIBLE
            tgFlightOrderReturnBookingCodeLabel.visibility = View.VISIBLE
            ivFlightOrderReturnBookingCodeCopy.visibility = View.VISIBLE

            tgFlightOrderReturnBookingCode.text = returnJourney.routes[0].pnr
            ivFlightOrderReturnBookingCodeCopy.setOnClickListener {
                listener?.onPnrCopyClicked(returnJourney.routes[0].pnr, true)
            }
        } else {
            tgFlightOrderReturnBookingCode.visibility = View.GONE
            tgFlightOrderReturnBookingCodeLabel.visibility = View.GONE
            ivFlightOrderReturnBookingCodeCopy.visibility = View.GONE
        }

        renderTerminalNotes(hasTerminal)
    }

    private fun hideReturnTicketView() {
        titleFlightOrderReturnTicket.visibility = View.GONE
        containerFlightOrderReturnTicket.visibility = View.GONE
    }

    private fun renderTerminalNotes(shouldShowTerminalNotes: Boolean) {
        tgFlightOrderTerminalNote.visibility = if (shouldShowTerminalNotes) View.VISIBLE else View.GONE
    }

    interface Listener {
        fun onPnrCopyClicked(pnr: String, isReturn: Boolean)
        fun onSendETicketClicked()
        fun onViewETicketClicked()
    }

    companion object{
        private const val HEX_CODE_TRANSPARENCY: Int = 0x00ffffff
    }
}