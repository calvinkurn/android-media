package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.databinding.ItemEventCheckoutPassengerBinding

class EventCheckoutPassengerDataAdapter : RecyclerView.Adapter<EventCheckoutPassengerDataAdapter.EventCheckoutPassengerViewHolder>() {

    private var listData = emptyList<String>()

    inner class EventCheckoutPassengerViewHolder(val binding: ItemEventCheckoutPassengerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String) {
            with(binding) {
                tgEventCheckoutPassenger.text = name
            }
        }
    }

    override fun getItemCount(): Int = listData.size
    override fun onBindViewHolder(holder: EventCheckoutPassengerViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventCheckoutPassengerViewHolder {
        val binding = ItemEventCheckoutPassengerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventCheckoutPassengerViewHolder(binding)
    }

    fun setList(list: List<String>) {
        listData = list
        notifyDataSetChanged()
    }
}
