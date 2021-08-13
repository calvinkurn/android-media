package com.tokopedia.flight.booking.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.data.FlightCartViewEntity
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_flight_booking_route_summary.view.*

/**
 * @author by jessica on 2019-10-28
 */

class FlightJourneyAdapter: RecyclerView.Adapter<FlightJourneyAdapter.ViewHolder>() {

    var journeys: List<FlightCartViewEntity.JourneySummary> = listOf()

    lateinit var listener: ViewHolder.ActionListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = journeys.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(journeys[position], position, listener)
    }

    fun updateRoutes(list: List<FlightCartViewEntity.JourneySummary>) {
        this.journeys = list
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(journey: FlightCartViewEntity.JourneySummary, position: Int, listener: ActionListener?) {

            with(view) {
                tv_flight_airline_name.text = journey.airline
                tv_flight_route_name.text = journey.routeName
                if (journey.isMultipleAirline) iv_flight_airlines_logo.setImageResource(R.drawable.ic_flight_multi_airlines) else iv_flight_airlines_logo.loadImage(journey.airlineLogo)
                tv_flight_route_date_detail.text = journey.date

                if (journey.isRefundable) {
                    iv_refundable.setImageResource(R.drawable.ic_flight_booking_refundable)
                    tv_refundable_info.text = context.getString(R.string.flight_label_refundable_info)
                } else {
                    iv_refundable.setImageResource(R.drawable.ic_flight_booking_non_refundable)
                    tv_refundable_info.text = context.getString(R.string.flight_label_non_refundable_info)
                }

                this.setOnClickListener {
                    listener?.onClickRouteDetail(journey.journeyId, position)
                }

                tv_transit_info.text = if (journey.transit == 0) context.getString(R.string.flight_booking_directly_trip_card)
                else String.format(context.getString(R.string.flight_booking_transit_with_value_trip_cart), journey.transit)
            }
        }

        companion object {
            val LAYOUT = R.layout.item_flight_booking_route_summary
        }

        interface ActionListener{
            fun onClickRouteDetail(id: String, position: Int)
        }
    }
}