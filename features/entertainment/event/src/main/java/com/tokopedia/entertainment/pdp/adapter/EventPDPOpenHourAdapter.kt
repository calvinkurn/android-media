package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.databinding.ItemEventPdpOpenHourBinding
import com.tokopedia.entertainment.pdp.data.pdp.OpenHour

class EventPDPOpenHourAdapter: RecyclerView.Adapter<EventPDPOpenHourAdapter.EventPDPOpenHourViewHolder>() {

    private var listOpenHour = emptyList<OpenHour>()

    inner class EventPDPOpenHourViewHolder(val binding: ItemEventPdpOpenHourBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(openHour: OpenHour) {
            with(binding) {
                tgEventPdpOpenDay.text = openHour.day
                tgEventPdpOpenHour.text = openHour.hour
            }
        }
    }

    override fun getItemCount(): Int = listOpenHour.size
    override fun onBindViewHolder(holder: EventPDPOpenHourViewHolder, position: Int) {
        holder.bind(listOpenHour[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPOpenHourViewHolder {
        val binding = ItemEventPdpOpenHourBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventPDPOpenHourViewHolder(binding)
    }

    fun setList(list: List<OpenHour>) {
        listOpenHour = list
        notifyDataSetChanged()
    }
}
