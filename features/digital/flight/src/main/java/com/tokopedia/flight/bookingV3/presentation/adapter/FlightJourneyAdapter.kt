package com.tokopedia.flight.bookingV3.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.bookingV3.data.FlightCart
import com.tokopedia.flight.bookingV3.data.FlightCartViewEntity
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_flight_booking_route_summary.view.*

/**
 * @author by jessica on 2019-10-28
 */

class FlightJourneyAdapter: RecyclerView.Adapter<FlightJourneyAdapter.ViewHolder>() {

    var journeys: List<FlightCartViewEntity.JourneySummary> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = journeys.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(journeys[position])
    }

    fun updateRoutes(list: List<FlightCartViewEntity.JourneySummary>) {
        this.journeys = list
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(journey: FlightCartViewEntity.JourneySummary) {
            view.tv_flight_airline_name.text = journey.airline
            view.tv_flight_route_name.text = journey.routeName
            view.iv_flight_airlines_logo.loadImage(journey.airlineLogo)

        }

        companion object {
            val LAYOUT = R.layout.item_flight_booking_route_summary
        }
    }
}