package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import kotlinx.android.synthetic.main.item_event_checkout_passenger.view.*

class EventCheckoutPassengerDataAdapter : RecyclerView.Adapter<EventCheckoutPassengerDataAdapter.EventCheckoutPassengerViewHolder>() {

    private var listData = emptyList<String>()

    inner class EventCheckoutPassengerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(name: String) {
            with(itemView) {
                tg_event_checkout_passenger.text = name
            }
        }
    }

    override fun getItemCount(): Int = listData.size
    override fun onBindViewHolder(holder: EventCheckoutPassengerViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventCheckoutPassengerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event_checkout_passenger, parent, false)
        return EventCheckoutPassengerViewHolder(itemView)
    }

    fun setList(list: List<String>) {
        listData = list
        notifyDataSetChanged()
    }
}