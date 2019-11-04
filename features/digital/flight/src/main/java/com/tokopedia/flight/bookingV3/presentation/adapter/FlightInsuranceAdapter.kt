package com.tokopedia.flight.bookingV3.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.bookingV3.data.FlightCart

/**
 * @author by jessica on 2019-11-01
 */

class FlightInsuranceAdapter: RecyclerView.Adapter<FlightInsuranceAdapter.ViewHolder>() {

    val insuranceList: List<FlightCart.Insurance> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = insuranceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(insuranceList[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(insurance: FlightCart.Insurance) {

        }

        companion object {
            val LAYOUT = R.layout.item_flight_booking_insurance
        }
    }
}