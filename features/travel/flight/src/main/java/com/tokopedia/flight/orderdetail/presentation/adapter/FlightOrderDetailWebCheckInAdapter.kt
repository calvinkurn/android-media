package com.tokopedia.flight.orderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.orderdetail.presentation.adapter.viewholder.FlightOrderDetailWebCheckInViewHolder
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailPassengerModel

/**
 * @author by furqan on 13/11/2020
 */
class FlightOrderDetailWebCheckInAdapter(private val journeyList: List<FlightOrderDetailJourneyModel>,
                                         private val passengerList: List<FlightOrderDetailPassengerModel>,
                                         private val listener: FlightOrderDetailWebCheckInViewHolder.Listener)
    : RecyclerView.Adapter<FlightOrderDetailWebCheckInViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightOrderDetailWebCheckInViewHolder =
            FlightOrderDetailWebCheckInViewHolder(LayoutInflater.from(parent.context)
                    .inflate(FlightOrderDetailWebCheckInViewHolder.LAYOUT, parent, false),
                    listener)

    override fun getItemCount(): Int = journeyList.size

    override fun onBindViewHolder(holder: FlightOrderDetailWebCheckInViewHolder, position: Int) {
        holder.bind(journeyList[position], passengerList, position == 0)
    }
}