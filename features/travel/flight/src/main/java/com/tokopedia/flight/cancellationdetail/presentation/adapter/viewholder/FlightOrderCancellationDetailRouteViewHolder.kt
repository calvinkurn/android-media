package com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.databinding.ItemFlightDetailBinding
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailRouteModel
import com.tokopedia.flight.search.presentation.util.FlightSearchCache
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.date.DateUtil

/**
 * @author by furqan on 07/01/2021
 */
class FlightOrderCancellationDetailRouteViewHolder(
        private val listener: Listener,
        private val isShowRefundableTag: Boolean,
        val binding: ItemFlightDetailBinding)
    : AbstractViewHolder<FlightOrderDetailRouteModel>(binding.root) {

    private lateinit var flightSearchCache: FlightSearchCache

    override fun bind(element: FlightOrderDetailRouteModel) {
        with(binding) {
            flightSearchCache = FlightSearchCache(itemView.context)

            headerDetailFlight.airlineName.text = element.airlineName
            headerDetailFlight.airlineCode.text = String.format("%s - %s", element.airlineId, element.flightNumber)
            setRefundableInfo(element)
            departureTime.text = DateUtil.formatDate(
                    DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    DateUtil.HH_MM,
                    element.departureTime)
            departureDate.text = DateUtil.formatDate(
                    DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    DateUtil.FORMAT_DATE,
                    element.departureTime)
            setColorCircle()
            setDepartureInfo(element)

            flightTime.text = element.duration
            arrivalTime.text = DateUtil.formatDate(
                    DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    DateUtil.HH_MM,
                    element.arrivalTime)
            arrivalDate.text = DateUtil.formatDate(
                    DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    DateUtil.FORMAT_DATE,
                    element.arrivalTime)
            setArrivalInfo(element)
            setPNR(element.pnr)

            headerDetailFlight.airlineIcon.loadImage(element.airlineLogo, R.drawable.flight_ic_airline_default)

            bindLastPosition(listener.getItemCount() == adapterPosition)
            bindTransitInfo(listener.getItemCount())

            if (element.stop > 0) {
                if (element.stopDetails.isNotEmpty()) {
                    tvFlightStopOver.visibility = View.VISIBLE
                    if (element.stopDetails.size < element.stop) {
                        tvFlightStopOver.text = String.format(getString(R.string.flight_detail_total_stop_over_label), element.stop)
                    } else {
                        tvFlightStopOver.text = getString(R.string.flight_detail_transit_stop_over_label)
                        tvFlightStopOver.append(" ")
                        tvFlightStopOver.append(element.stopDetails.joinToString(", "))
                    }
                } else {
                    tvFlightStopOver.visibility = View.GONE
                }
            } else {
                tvFlightStopOver.visibility = View.GONE
            }

            if (element.departureTerminal.isNotEmpty()) {
                departureTerminal.text = getString(R.string.flight_terminal_info, element.departureTerminal)
                departureTerminal.visibility = View.VISIBLE
            } else {
                departureTerminal.visibility = View.GONE
            }

            if (element.arrivalTerminal.isNotEmpty()) {
                arrivalTerminal.text = getString(R.string.flight_terminal_info, element.arrivalTerminal)
                arrivalTerminal.visibility = View.VISIBLE
            } else {
                arrivalTerminal.visibility = View.GONE
            }

            if (element.operatorAirlineId.isNotEmpty()) {
                headerDetailFlight.airlineOperatingBy.text = getString(R.string.flight_detail_operating_by, element.operatorAirlineId)
                headerDetailFlight.airlineOperatingBy.visibility = View.VISIBLE
            } else {
                headerDetailFlight.airlineOperatingBy.visibility = View.GONE
            }
        }
    }

    private fun setRefundableInfo(element: FlightOrderDetailRouteModel) {
        with(binding) {
            if (element.refundable) {
                headerDetailFlight.airlineRefundableInfo.setText(R.string.flight_label_refundable_info)
            } else {
                headerDetailFlight.airlineRefundableInfo.setText(R.string.flight_label_non_refundable_info)
            }
            if (isShowRefundableTag) headerDetailFlight.airlineRefundableInfo.visibility = View.VISIBLE
            else headerDetailFlight.airlineRefundableInfo.visibility = View.GONE
        }
    }

    //set color circle to green if position holder is on first index
    private fun setColorCircle() {
        with(binding) {
            if (adapterPosition == 0) {
                departureTimeCircle.isEnabled = true
            }
        }
    }

    private fun setDepartureInfo(route: FlightOrderDetailRouteModel) {
        with(binding) {
            if (!TextUtils.isEmpty(route.departureCityName)) {
                departureAirportName.text = String.format("%s (%s)", route.departureCityName, route.departureId)
                departureDescAirportName.text = route.departureAirportName
            } else {
                arrivalAirportName.text = (route.departureId)
                arrivalDescAirportName.text = ""
            }
        }
    }

    private fun setArrivalInfo(route: FlightOrderDetailRouteModel) {
        with(binding) {
            var transitTag = ""
            if (!TextUtils.isEmpty(flightSearchCache.getInternationalTransitTag())) {
                transitTag = flightSearchCache.getInternationalTransitTag()
            }

            val arrivalAirport: String = if (!TextUtils.isEmpty(route.arrivalCityName)) {
                arrivalDescAirportName.text = route.arrivalAirportName
                arrivalAirportName.text = String.format("%s (%s)", route.arrivalCityName, route.arrivalId)
                route.arrivalCityName
            } else {
                arrivalAirportName.text = route.arrivalId
                arrivalDescAirportName.text = ""
                route.arrivalId
            }
            if (route.layover.isNotEmpty()) {
                transitInfo.setTextDescription(itemView.context.getString(R.string.flight_label_transit_with_duration,
                        arrivalAirport, route.layover, transitTag))
            } else {
                transitInfo.setTextDescription(itemView.context.getString(R.string.flight_label_transit_without_duration,
                        arrivalAirport, transitTag))
            }
        }
    }

    private fun setPNR(pnr: String) {
        with(binding) {
            if (!TextUtils.isEmpty(pnr)) {
                headerDetailFlight.containerPnr.visibility = View.VISIBLE
                headerDetailFlight.pnrCode.text = pnr
                headerDetailFlight.copyPnr.setOnClickListener {
                    val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(getString(R.string.flight_label_order_id), headerDetailFlight.pnrCode.text.toString())
                    clipboard.setPrimaryClip(clip)
                    clipboard.addPrimaryClipChangedListener {
                        Toaster.build(itemView, itemView.context.getString(R.string.flight_label_copy_clipboard), Toaster.LENGTH_SHORT).show()
                    }
                }
            } else {
                headerDetailFlight.containerPnr.visibility = View.GONE
            }
        }
    }

    //set color circle to red if position holder is on last index
    fun bindLastPosition(lastItemPosition: Boolean) {
        with(binding) {
            if (lastItemPosition) {
                arrivalTimeCircle.isEnabled = false
            }
        }
    }

    //set visible transit info if flight have transit and position holder is on first index
    fun bindTransitInfo(sizeInfo: Int) {
        with(binding) {
            if (sizeInfo > 0 && adapterPosition < sizeInfo - 1) {
                transitInfo.visibility = View.VISIBLE
            } else {
                transitInfo.visibility = View.GONE
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