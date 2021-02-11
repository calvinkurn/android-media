package com.tokopedia.flight.orderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.orderdetail.presentation.adapter.viewholder.FlightOrderDetailPassengerCancelStatusViewHolder
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailPassengerCancelStatusModel

/**
 * @author by furqan on 30/10/2020
 */
class FlightOrderDetailPassengerCancelStatusAdapter(private val itemList: List<FlightOrderDetailPassengerCancelStatusModel>)
    : RecyclerView.Adapter<FlightOrderDetailPassengerCancelStatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightOrderDetailPassengerCancelStatusViewHolder =
            FlightOrderDetailPassengerCancelStatusViewHolder(LayoutInflater.from(parent.context)
                    .inflate(FlightOrderDetailPassengerCancelStatusViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: FlightOrderDetailPassengerCancelStatusViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

}