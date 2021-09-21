package com.tokopedia.flight.booking.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.data.FlightCartViewEntity
import com.tokopedia.flight.databinding.ItemFlightBookingRouteSummaryBinding
import com.tokopedia.kotlin.extensions.view.loadImage

/**
 * @author by jessica on 2019-10-28
 */

class FlightJourneyAdapter: RecyclerView.Adapter<FlightJourneyAdapter.ViewHolder>() {

    var journeys: List<FlightCartViewEntity.JourneySummary> = listOf()

    lateinit var listener: ViewHolder.ActionListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemFlightBookingRouteSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = journeys.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(journeys[position], position, listener)
    }

    fun updateRoutes(list: List<FlightCartViewEntity.JourneySummary>) {
        this.journeys = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemFlightBookingRouteSummaryBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(journey: FlightCartViewEntity.JourneySummary, position: Int, listener: ActionListener?) {

            with(binding) {
                tvFlightAirlineName.text = journey.airline
                tvFlightRouteName.text = journey.routeName
                if (journey.isMultipleAirline) ivFlightAirlinesLogo.setImageResource(R.drawable.ic_flight_multi_airlines) else ivFlightAirlinesLogo.loadImage(journey.airlineLogo)
                tvFlightRouteDateDetail.text = journey.date

                if (journey.isRefundable) {
                    ivRefundable.setImageResource(R.drawable.ic_flight_booking_refundable)
                    tvRefundableInfo.text = itemView.context.getString(R.string.flight_label_refundable_info)
                } else {
                    ivRefundable.setImageResource(R.drawable.ic_flight_booking_non_refundable)
                    tvRefundableInfo.text = itemView.context.getString(R.string.flight_label_non_refundable_info)
                }

                this.root.setOnClickListener {
                    listener?.onClickRouteDetail(journey.journeyId, position)
                }

                tvTransitInfo.text = if (journey.transit == 0) itemView.context.getString(R.string.flight_booking_directly_trip_card)
                else String.format(itemView.context.getString(R.string.flight_booking_transit_with_value_trip_cart), journey.transit)
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