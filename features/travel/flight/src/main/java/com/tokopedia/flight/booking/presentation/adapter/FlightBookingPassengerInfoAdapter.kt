package com.tokopedia.flight.booking.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.detail.view.model.SimpleModel
import kotlinx.android.synthetic.main.item_flight_booking_v3_passenger_detail.view.*

/**
 * @author by jessica on 2019-11-04
 */

class FlightBookingPassengerInfoAdapter: RecyclerView.Adapter<FlightBookingPassengerInfoAdapter.ViewHolder>() {

    var list: List<SimpleModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateList(list: List<SimpleModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(item: SimpleModel) {
            with(view) {
                tv_title.text = item.label
                tv_description.text = item.description
            }
        }

        companion object {
            val LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_v3_passenger_detail
        }
    }

}

