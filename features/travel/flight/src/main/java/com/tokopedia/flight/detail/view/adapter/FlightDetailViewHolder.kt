package com.tokopedia.flight.detail.view.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.flight.R
import com.tokopedia.flight.detail.view.adapter.FlightDetailAdapterTypeFactory.OnFlightDetailListener
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel
import com.tokopedia.flight.search.presentation.util.FlightSearchCache
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.DateUtil

/**
 * Created by furqan on 06/10/21.
 */
class FlightDetailViewHolder(itemView: View,
                             private val onFlightDetailListener: OnFlightDetailListener?,
                             private val isShowRefundableTag: Boolean)
    : AbstractViewHolder<FlightDetailRouteModel>(itemView) {

    private val flightSearchCache: FlightSearchCache = FlightSearchCache(itemView.context)

    private val imageAirline: ImageView = itemView.findViewById(R.id.airline_icon)
    private val airlineName: TextView = itemView.findViewById(R.id.airline_name)
    private val stopOverTextView: TextView = itemView.findViewById(R.id.tv_flight_stop_over)
    private val airlineCode: TextView = itemView.findViewById(R.id.airline_code)
    private val airlineOperatingBy: Typography = itemView.findViewById(R.id.airline_operating_by)
    private val refundableInfo: TextView = itemView.findViewById(R.id.airline_refundable_info)
    private val departureTime: TextView = itemView.findViewById(R.id.departure_time)
    private val departureDate: TextView = itemView.findViewById(R.id.departure_date)
    private val departureCircleImage: ImageView = itemView.findViewById(R.id.departure_time_circle)
    private val departureAirportName: TextView = itemView.findViewById(R.id.departure_airport_name)
    private val departureAirportDesc: TextView = itemView.findViewById(R.id.departure_desc_airport_name)
    private val departureTerminal: TextView = itemView.findViewById(R.id.departure_terminal)
    private val flightTime: TextView = itemView.findViewById(R.id.flight_time)
    private val arrivalTime: TextView = itemView.findViewById(R.id.arrival_time)
    private val arrivalDate: TextView = itemView.findViewById(R.id.arrival_date)
    private val arrivalCircleImage: ImageView = itemView.findViewById(R.id.arrival_time_circle)
    private val arrivalAirportName: TextView = itemView.findViewById(R.id.arrival_airport_name)
    private val arrivalAirportDesc: TextView = itemView.findViewById(R.id.arrival_desc_airport_name)
    private val arrivalTerminal: TextView = itemView.findViewById(R.id.arrival_terminal)
    private val transitInfo: Ticker = itemView.findViewById(R.id.transit_info)
    private val containerPNR: View = itemView.findViewById(R.id.container_pnr)
    private val pnrCode: TextView = itemView.findViewById(R.id.pnr_code)
    private val copyPnr: ImageView = itemView.findViewById(R.id.copy_pnr)

    override fun bind(route: FlightDetailRouteModel) {
        airlineName.text = route.airlineName
        airlineCode.text = String.format("%s - %s", route.airlineCode, route.flightNumber)
        setRefundableInfo(route)
        departureTime.text = DateUtil.formatDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, DateUtil.HH_MM, route.departureTimestamp)
        departureDate.text = DateUtil.formatDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, DateUtil.FORMAT_DATE, route.departureTimestamp)
        setColorCircle()
        setDepartureInfo(route)
        flightTime.text = route.duration
        arrivalTime.text = DateUtil.formatDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, DateUtil.HH_MM, route.arrivalTimestamp)
        arrivalDate.text = DateUtil.formatDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, DateUtil.FORMAT_DATE, route.arrivalTimestamp)
        setArrivalInfo(route)
        setPNR(route.pnr)
        ImageHandler.loadImageWithoutPlaceholder(imageAirline, route.airlineLogo,
                ContextCompat.getDrawable(itemView.context, R.drawable.flight_ic_airline_default)
        )
        if (onFlightDetailListener != null) {
            bindLastPosition(onFlightDetailListener.getItemCount() == adapterPosition)
            bindTransitInfo(onFlightDetailListener.getItemCount())
        }
        if (route.stopOver > 0) {
            if (route.stopOverDetail.isNotEmpty()) {
                stopOverTextView.visibility = View.VISIBLE
                if (route.stopOverDetail.size < route.stopOver) {
                    stopOverTextView.text = String.format(getString(R.string.flight_detail_total_stop_over_label), route.stopOver)
                } else {
                    stopOverTextView.text = getString(R.string.flight_detail_transit_stop_over_label)
                    stopOverTextView.append(" ")
                    stopOverTextView.append(TextUtils.join(", ", route.stopOverDetail))
                }
            } else {
                stopOverTextView.visibility = View.GONE
            }
        } else {
            stopOverTextView.visibility = View.GONE
        }
        if (route.departureTerminal.isNotEmpty()) {
            departureTerminal.text = getString(R.string.flight_terminal_info, route.departureTerminal)
            departureTerminal.visibility = View.VISIBLE
        } else {
            departureTerminal.visibility = View.GONE
        }
        if (route.arrivalTerminal.isNotEmpty()) {
            arrivalTerminal.text = getString(R.string.flight_terminal_info, route.arrivalTerminal)
            arrivalTerminal.visibility = View.VISIBLE
        } else {
            arrivalTerminal.visibility = View.GONE
        }
        if (route.operatingAirline.isNotEmpty()) {
            airlineOperatingBy.text = getString(R.string.flight_detail_operating_by, route.operatingAirline)
            airlineOperatingBy.visibility = View.VISIBLE
        } else {
            airlineOperatingBy.visibility = View.GONE
        }
    }

    private fun setDepartureInfo(route: FlightDetailRouteModel) {
        if (!TextUtils.isEmpty(route.departureAirportCity)) {
            departureAirportName.text = String.format("%s (%s)", route.departureAirportCity, route.departureAirportCode)
            departureAirportDesc.text = route.departureAirportName
        } else {
            departureAirportName.text = route.departureAirportCode
            departureAirportDesc.text = ""
        }
    }

    private fun setArrivalInfo(route: FlightDetailRouteModel) {
        var transitTag = ""
        if (!TextUtils.isEmpty(flightSearchCache.getInternationalTransitTag())) {
            transitTag = flightSearchCache.getInternationalTransitTag()
        }
        val arrivalAirport: String =
                if (!TextUtils.isEmpty(route.arrivalAirportCity)) {
                    arrivalAirportDesc.text = route.arrivalAirportName
                    arrivalAirportName.text = String.format("%s (%s)", route.arrivalAirportCity, route.arrivalAirportCode)
                    route.arrivalAirportCity
                } else {
                    arrivalAirportName.text = route.arrivalAirportCode
                    arrivalAirportDesc.text = ""
                    route.arrivalAirportCode
                }
        if (route.layover.isNotEmpty()) {
            transitInfo.setTextDescription(itemView.context.getString(R.string.flight_label_transit_with_duration,
                    arrivalAirport, route.layover, transitTag))
        } else {
            transitInfo.setTextDescription(itemView.context.getString(R.string.flight_label_transit_without_duration,
                    arrivalAirport, transitTag))
        }
    }

    private fun setPNR(pnr: String) {
        if (!TextUtils.isEmpty(pnr)) {
            containerPNR.visibility = View.VISIBLE
            pnrCode.text = pnr
            copyPnr.setOnClickListener {
                val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(getString(R.string.flight_label_order_id), pnrCode.text.toString())
                clipboard.setPrimaryClip(clip)
                clipboard.addPrimaryClipChangedListener { Toast.makeText(itemView.context, R.string.flight_label_copy_clipboard, Toast.LENGTH_SHORT).show() }
            }
        } else {
            containerPNR.visibility = View.GONE
        }
    }

    private fun setRefundableInfo(route: FlightDetailRouteModel) {
        if (route.isRefundable) {
            refundableInfo.setText(R.string.flight_label_refundable_info)
        } else {
            refundableInfo.setText(R.string.flight_label_non_refundable_info)
        }
        if (isShowRefundableTag) refundableInfo.visibility = View.VISIBLE else refundableInfo.visibility = View.GONE
    }

    //set color circle to green if position holder is on first index
    private fun setColorCircle() {
        if (adapterPosition == 0) {
            departureCircleImage.isEnabled = true
        }
    }

    //set color circle to red if position holder is on last index
    private fun bindLastPosition(lastItemPosition: Boolean) {
        if (lastItemPosition) {
            arrivalCircleImage.isEnabled = false
        }
    }

    //set visible transit info if flight have transit and position holder is on first index
    private fun bindTransitInfo(sizeInfo: Int) {
        if (sizeInfo > 0 && adapterPosition < sizeInfo - 1) {
            transitInfo.visibility = View.VISIBLE
        } else {
            transitInfo.visibility = View.GONE
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_flight_detail
    }

}