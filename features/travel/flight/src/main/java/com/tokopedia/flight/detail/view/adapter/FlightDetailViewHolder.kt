package com.tokopedia.flight.detail.view.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.databinding.ItemFlightDetailBinding
import com.tokopedia.flight.detail.view.adapter.FlightDetailAdapterTypeFactory.OnFlightDetailListener
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel
import com.tokopedia.flight.search.presentation.util.FlightSearchCache
import com.tokopedia.media.loader.loadImageWithError
import com.tokopedia.utils.date.DateUtil

/**
 * Created by furqan on 06/10/21.
 */
class FlightDetailViewHolder(val binding: ItemFlightDetailBinding,
                             private val onFlightDetailListener: OnFlightDetailListener?,
                             private val isShowRefundableTag: Boolean)
    : AbstractViewHolder<FlightDetailRouteModel>(binding.root) {

    private val flightSearchCache: FlightSearchCache = FlightSearchCache(itemView.context)

    override fun bind(route: FlightDetailRouteModel) {
        with(binding){
            headerDetailFlight.airlineName.text = route.airlineName
            headerDetailFlight.airlineCode.text = String.format("%s - %s", route.airlineCode, route.flightNumber)
            setRefundableInfo(route)
            departureTime.text = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.HH_MM,
                route.departureTimestamp
            )
            departureDate.text = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.FORMAT_DATE,
                route.departureTimestamp
            )
            setColorCircle()
            setDepartureInfo(route)
            flightTime.text = route.duration
            arrivalTime.text = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.HH_MM,
                route.arrivalTimestamp
            )
            arrivalDate.text = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.FORMAT_DATE,
                route.arrivalTimestamp
            )
            setArrivalInfo(route)
            setPNR(route.pnr)
            headerDetailFlight.airlineIcon.loadImageWithError(route.airlineLogo, R.drawable.flight_ic_airline_default)
            if (onFlightDetailListener != null) {
                bindLastPosition(onFlightDetailListener.getItemCount() == adapterPosition)
                bindTransitInfo(onFlightDetailListener.getItemCount())
            }
            if (route.stopOver > 0) {
                if (route.stopOverDetail.isNotEmpty()) {
                    tvFlightStopOver.visibility = View.VISIBLE
                    if (route.stopOverDetail.size < route.stopOver) {
                        tvFlightStopOver.text = String.format(
                            getString(R.string.flight_detail_total_stop_over_label),
                            route.stopOver
                        )
                    } else {
                        tvFlightStopOver.text =
                            getString(R.string.flight_detail_transit_stop_over_label)
                        tvFlightStopOver.append(" ")
                        tvFlightStopOver.append(TextUtils.join(", ", route.stopOverDetail))
                    }
                } else {
                    tvFlightStopOver.visibility = View.GONE
                }
            } else {
                tvFlightStopOver.visibility = View.GONE
            }
            if (route.departureTerminal.isNotEmpty()) {
                departureTerminal.text =
                    getString(R.string.flight_terminal_info, route.departureTerminal)
                departureTerminal.visibility = View.VISIBLE
            } else {
                departureTerminal.visibility = View.GONE
            }
            if (route.arrivalTerminal.isNotEmpty()) {
                arrivalTerminal.text =
                    getString(R.string.flight_terminal_info, route.arrivalTerminal)
                arrivalTerminal.visibility = View.VISIBLE
            } else {
                arrivalTerminal.visibility = View.GONE
            }
            if (route.operatingAirline.isNotEmpty()) {
                headerDetailFlight.airlineOperatingBy.text =
                    getString(R.string.flight_detail_operating_by, route.operatingAirline)
                headerDetailFlight.airlineOperatingBy.visibility = View.VISIBLE
            } else {
                headerDetailFlight.airlineOperatingBy.visibility = View.GONE
            }
        }
    }

    private fun setDepartureInfo(route: FlightDetailRouteModel) {
        with(binding){
            if (!TextUtils.isEmpty(route.departureAirportCity)) {
                departureAirportName.text =
                    String.format("%s (%s)", route.departureAirportCity, route.departureAirportCode)
                departureDescAirportName.text = route.departureAirportName
            } else {
                departureAirportName.text = route.departureAirportCode
                departureDescAirportName.text = ""
            }
        }
    }

    private fun setArrivalInfo(route: FlightDetailRouteModel) {
        with(binding){
            var transitTag = ""
            if (!TextUtils.isEmpty(flightSearchCache.getInternationalTransitTag())) {
                transitTag = flightSearchCache.getInternationalTransitTag()
            }
            val arrivalAirport: String =
                if (!TextUtils.isEmpty(route.arrivalAirportCity)) {
                    arrivalDescAirportName.text = route.arrivalAirportName
                    arrivalAirportName.text =
                        String.format("%s (%s)", route.arrivalAirportCity, route.arrivalAirportCode)
                    route.arrivalAirportCity
                } else {
                    arrivalAirportName.text = route.arrivalAirportCode
                    arrivalDescAirportName.text = ""
                    route.arrivalAirportCode
                }
            if (route.layover.isNotEmpty()) {
                transitInfo.setTextDescription(
                    itemView.context.getString(
                        R.string.flight_label_transit_with_duration,
                        arrivalAirport, route.layover, transitTag
                    )
                )
            } else {
                transitInfo.setTextDescription(
                    itemView.context.getString(
                        R.string.flight_label_transit_without_duration,
                        arrivalAirport, transitTag
                    )
                )
            }
        }
    }

    private fun setPNR(pnr: String) {
        with(binding){
            if (!TextUtils.isEmpty(pnr)) {
                headerDetailFlight.containerPnr.visibility = View.VISIBLE
                headerDetailFlight.pnrCode.text = pnr
                headerDetailFlight.copyPnr.setOnClickListener {
                    val clipboard =
                        itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(
                        getString(R.string.flight_label_order_id),
                        headerDetailFlight.pnrCode.text.toString()
                    )
                    clipboard.setPrimaryClip(clip)
                    clipboard.addPrimaryClipChangedListener {
                        Toast.makeText(
                            itemView.context,
                            R.string.flight_label_copy_clipboard,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                headerDetailFlight.containerPnr.visibility = View.GONE
            }
        }
    }

    private fun setRefundableInfo(route: FlightDetailRouteModel) {
        with(binding){
            if (route.isRefundable) {
                headerDetailFlight.airlineRefundableInfo.setText(R.string.flight_label_refundable_info)
            } else {
                headerDetailFlight.airlineRefundableInfo.setText(R.string.flight_label_non_refundable_info)
            }
            if (isShowRefundableTag) headerDetailFlight.airlineRefundableInfo.visibility =
                View.VISIBLE else headerDetailFlight.airlineRefundableInfo.visibility = View.GONE
        }
    }

    //set color circle to green if position holder is on first index
    private fun setColorCircle() {
        if (adapterPosition == 0) {
            binding.departureTimeCircle.isEnabled = true
        }
    }

    //set color circle to red if position holder is on last index
    private fun bindLastPosition(lastItemPosition: Boolean) {
        if (lastItemPosition) {
            binding.arrivalTimeCircle.isEnabled = false
        }
    }

    //set visible transit info if flight have transit and position holder is on first index
    private fun bindTransitInfo(sizeInfo: Int) {
        with(binding) {
            if (sizeInfo > 0 && adapterPosition < sizeInfo - 1) {
                transitInfo.visibility = View.VISIBLE
            } else {
                transitInfo.visibility = View.GONE
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_flight_detail
    }

}
