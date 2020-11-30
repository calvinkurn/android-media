package com.tokopedia.flight.orderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.orderdetail.presentation.adapter.viewholder.FlightOrderDetailPassengerViewHolder
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailPassengerModel

/**
 * @author by furqan on 30/10/2020
 */
class FlightOrderDetailPassengerAdapter(private val itemList: List<FlightOrderDetailPassengerModel>)
    : RecyclerView.Adapter<FlightOrderDetailPassengerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightOrderDetailPassengerViewHolder =
            FlightOrderDetailPassengerViewHolder(LayoutInflater.from(parent.context)
                    .inflate(FlightOrderDetailPassengerViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: FlightOrderDetailPassengerViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}