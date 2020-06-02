package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.pdp.OpenHour
import kotlinx.android.synthetic.main.item_event_pdp_open_hour.view.*

class EventPDPOpenHourAdapter: RecyclerView.Adapter<EventPDPOpenHourAdapter.EventPDPOpenHourViewHolder>() {

    private var listOpenHour = emptyList<OpenHour>()

    inner class EventPDPOpenHourViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(openHour: OpenHour) {
            with(itemView) {
                tg_event_pdp_open_day.text = openHour.day
                tg_event_pdp_open_hour.text = openHour.hour
            }
        }
    }

    override fun getItemCount(): Int = listOpenHour.size
    override fun onBindViewHolder(holder: EventPDPOpenHourViewHolder, position: Int) {
        holder.bind(listOpenHour[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPOpenHourViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event_pdp_open_hour, parent, false)
        return EventPDPOpenHourViewHolder(itemView)
    }

    fun setList(list: List<OpenHour>) {
        listOpenHour = list
        notifyDataSetChanged()
    }
}