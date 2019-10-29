package com.tokopedia.flight.bookingV3.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.flight.R
import com.tokopedia.flight.bookingV3.data.FlightCart
import kotlinx.android.synthetic.main.item_flight_booking_route_summary.view.*

/**
 * @author by jessica on 2019-10-28
 */

class FlightJourneyAdapter: RecyclerView.Adapter<FlightJourneyAdapter.ViewHolder>() {

    var journeys: List<FlightCart.Journey> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = journeys.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(journeys[position])
    }

    fun updateRoutes(list: List<FlightCart.Journey>) {
        this.journeys = list
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(journey: FlightCart.Journey) {
            view.tv_flight_airline_name.text = String.format("%s -> %s", journey.departureAirportId, journey.arrivalAirportId)
        }

        companion object {
            val LAYOUT = R.layout.item_flight_booking_route_summary
        }
    }
}