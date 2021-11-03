package com.tokopedia.flight.booking.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.databinding.ItemFlightBookingV3PassengerDetailBinding
import com.tokopedia.flight.detail.view.model.SimpleModel

/**
 * @author by jessica on 2019-11-04
 */

class FlightBookingPassengerInfoAdapter: RecyclerView.Adapter<FlightBookingPassengerInfoAdapter.ViewHolder>() {

    var list: List<SimpleModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemFlightBookingV3PassengerDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateList(list: List<SimpleModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemFlightBookingV3PassengerDetailBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SimpleModel) {
            with(binding) {
                tvTitle.text = item.label
                tvDescription.text = item.description
            }
        }

        companion object {
            val LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_v3_passenger_detail
        }
    }

}

