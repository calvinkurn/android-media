package com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailRouteModel
import com.tokopedia.flight.search.presentation.util.FlightSearchCache
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.include_header_detail_flight.view.*
import kotlinx.android.synthetic.main.item_flight_detail.view.*

/**
 * @author by furqan on 07/01/2021
 */
class FlightOrderCancellationDetailRouteViewHolder(
        private val listener: Listener,
        private val isShowRefundableTag: Boolean,
        view: View)
    : AbstractViewHolder<FlightOrderDetailRouteModel>(view) {

    private lateinit var flightSearchCache: FlightSearchCache

    override fun bind(element: FlightOrderDetailRouteModel) {
        with(itemView) {
            flightSearchCache = FlightSearchCache(context)

            airline_name.text = element.airlineName
            airline_code.text = String.format("%s - %s", element.airlineId, element.flightNumber)
            setRefundableInfo(element)
            departure_time.text = FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    FlightDateUtil.FORMAT_TIME_DETAIL, element.departureTime)
            departure_date.text = FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    FlightDateUtil.FORMAT_DATE_LOCAL_DETAIL, element.departureTime)
            setColorCircle()
            setDepartureInfo(element)

            flight_time.text = element.duration
            arrival_time.text = FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    FlightDateUtil.FORMAT_TIME_DETAIL, element.arrivalTime)
            arrival_date.text = FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    FlightDateUtil.FORMAT_DATE_LOCAL_DETAIL, element.arrivalTime)
            setArrivalInfo(element)
            setPNR(element.pnr)

            airline_icon.loadImage(element.airlineLogo, R.drawable.flight_ic_airline_default)

            bindLastPosition(listener.getItemCount() == adapterPosition)
            bindTransitInfo(listener.getItemCount())

            if (element.stop > 0) {
                if (element.stopDetails.isNotEmpty()) {
                    tv_flight_stop_over.visibility = View.VISIBLE
                    if (element.stopDetails.size < element.stop) {
                        tv_flight_stop_over.text = String.format(getString(R.string.flight_detail_total_stop_over_label), element.stop)
                    } else {
                        tv_flight_stop_over.text = getString(R.string.flight_detail_transit_stop_over_label)
                        tv_flight_stop_over.append(" ")
                        tv_flight_stop_over.append(element.stopDetails.joinToString(", "))
                    }
                } else {
                    tv_flight_stop_over.visibility = View.GONE
                }
            } else {
                tv_flight_stop_over.visibility = View.GONE
            }

            if (element.departureTerminal.isNotEmpty()) {
                departure_terminal.text = getString(R.string.flight_terminal_info, element.departureTerminal)
                departure_terminal.visibility = View.VISIBLE
            } else {
                departure_terminal.visibility = View.GONE
            }

            if (element.arrivalTerminal.isNotEmpty()) {
                arrival_terminal.text = getString(R.string.flight_terminal_info, element.arrivalTerminal)
                arrival_terminal.visibility = View.VISIBLE
            } else {
                arrival_terminal.visibility = View.GONE
            }

            if (element.operatorAirlineId.isNotEmpty()) {
                airline_operating_by.text = getString(R.string.flight_detail_operating_by, element.operatorAirlineId)
                airline_operating_by.visibility = View.VISIBLE
            } else {
                airline_operating_by.visibility = View.GONE
            }
        }
    }

    private fun setRefundableInfo(element: FlightOrderDetailRouteModel) {
        with(itemView) {
            if (element.refundable) {
                airline_refundable_info.setText(R.string.flight_label_refundable_info)
            } else {
                airline_refundable_info.setText(R.string.flight_label_non_refundable_info)
            }
            if (isShowRefundableTag) airline_refundable_info.visibility = View.VISIBLE
            else airline_refundable_info.visibility = View.GONE
        }
    }

    //set color circle to green if position holder is on first index
    private fun setColorCircle() {
        with(itemView) {
            if (adapterPosition == 0) {
                departure_time_circle.isEnabled = true
            }
        }
    }

    private fun setDepartureInfo(route: FlightOrderDetailRouteModel) {
        with(itemView) {
            if (!TextUtils.isEmpty(route.departureCityName)) {
                departure_airport_name.text = String.format("%s (%s)", route.departureCityName, route.departureId)
                departure_desc_airport_name.text = route.departureAirportName
            } else {
                departure_airport_name.text = (route.departureId)
                departure_desc_airport_name.text = ""
            }
        }
    }

    private fun setArrivalInfo(route: FlightOrderDetailRouteModel) {
        with(itemView) {
            var transitTag = ""
            if (!TextUtils.isEmpty(flightSearchCache.getInternationalTransitTag())) {
                transitTag = flightSearchCache.getInternationalTransitTag()
            }

            var arrivalAirport: String = if (!TextUtils.isEmpty(route.arrivalCityName)) {
                arrival_desc_airport_name.text = route.arrivalAirportName
                arrival_airport_name.text = String.format("%s (%s)", route.arrivalCityName, route.arrivalId)
                route.arrivalCityName
            } else {
                arrival_airport_name.text = route.arrivalId
                arrival_desc_airport_name.text = ""
                route.arrivalId
            }
            if (route.layover.isNotEmpty()) {
                transit_info.setTextDescription(itemView.context.getString(R.string.flight_label_transit_with_duration,
                        arrivalAirport, route.layover, transitTag))
            } else {
                transit_info.setTextDescription(itemView.context.getString(R.string.flight_label_transit_without_duration,
                        arrivalAirport, transitTag))
            }
        }
    }

    private fun setPNR(pnr: String) {
        with(itemView) {
            if (!TextUtils.isEmpty(pnr)) {
                container_pnr.visibility = View.VISIBLE
                pnr_code.text = pnr
                copy_pnr.setOnClickListener {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(getString(R.string.flight_label_order_id), pnr_code.text.toString())
                    clipboard.setPrimaryClip(clip)
                    clipboard.addPrimaryClipChangedListener {
                        Toaster.build(itemView, context.getString(R.string.flight_label_copy_clipboard), Toaster.LENGTH_SHORT).show()
                    }
                }
            } else {
                container_pnr.visibility = View.GONE
            }
        }
    }

    //set color circle to red if position holder is on last index
    fun bindLastPosition(lastItemPosition: Boolean) {
        with(itemView) {
            if (lastItemPosition) {
                arrival_time_circle.isEnabled = false
            }
        }
    }

    //set visible transit info if flight have transit and position holder is on first index
    fun bindTransitInfo(sizeInfo: Int) {
        with(itemView) {
            if (sizeInfo > 0 && adapterPosition < sizeInfo - 1) {
                transit_info.visibility = View.VISIBLE
            } else {
                transit_info.visibility = View.GONE
            }
        }
    }

    interface Listener {
        fun getItemCount(): Int
    }

    companion object {
        val LAYOUT = R.layout.item_flight_detail
    }
}