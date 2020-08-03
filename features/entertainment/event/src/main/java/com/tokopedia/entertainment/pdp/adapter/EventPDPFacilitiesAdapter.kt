package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.Facilities
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_event_pdp_facilities.view.*

class EventPDPFacilitiesAdapter: RecyclerView.Adapter<EventPDPFacilitiesAdapter.EventPDPFacilitiesViewHolder>() {

    private var listFacilities = emptyList<Facilities>()

    inner class EventPDPFacilitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(facilities: Facilities) {
            with(itemView) {
                tg_event_pdp_facilitites.text = facilities.title
                iv_event_pdp_facilities.loadImage(facilities.iconUrl)
            }
        }
    }

    override fun getItemCount(): Int = listFacilities.size
    override fun onBindViewHolder(holder: EventPDPFacilitiesViewHolder, position: Int) {
        holder.bind(listFacilities[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPFacilitiesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event_pdp_facilities, parent, false)
        return EventPDPFacilitiesViewHolder(itemView)
    }

    fun setList(list: List<Facilities>) {
        listFacilities = list
        notifyDataSetChanged()
    }
}