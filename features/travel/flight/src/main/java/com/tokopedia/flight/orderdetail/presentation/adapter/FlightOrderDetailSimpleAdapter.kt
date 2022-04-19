package com.tokopedia.flight.orderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.databinding.ItemFlightOrderDetailSimpleBinding
import com.tokopedia.flight.orderdetail.presentation.adapter.viewholder.FlightOrderDetailSimpleViewHolder
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailSimpleModel

/**
 * @author by furqan on 12/11/2020
 */
class FlightOrderDetailSimpleAdapter(private val itemList: List<FlightOrderDetailSimpleModel>)
    : RecyclerView.Adapter<FlightOrderDetailSimpleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightOrderDetailSimpleViewHolder =
            FlightOrderDetailSimpleViewHolder(
                ItemFlightOrderDetailSimpleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: FlightOrderDetailSimpleViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}