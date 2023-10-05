package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.databinding.ItemEventPdpFacilitiesBinding
import com.tokopedia.entertainment.pdp.data.Facilities
import com.tokopedia.media.loader.loadImage

class EventPDPFacilitiesAdapter: RecyclerView.Adapter<EventPDPFacilitiesAdapter.EventPDPFacilitiesViewHolder>() {

    private var listFacilities = emptyList<Facilities>()

    inner class EventPDPFacilitiesViewHolder(val binding: ItemEventPdpFacilitiesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(facilities: Facilities) {
            with(binding) {
                tgEventPdpFacilitites.text = facilities.title
                ivEventPdpFacilities.loadImage(facilities.iconUrl)
            }
        }
    }

    override fun getItemCount(): Int = listFacilities.size
    override fun onBindViewHolder(holder: EventPDPFacilitiesViewHolder, position: Int) {
        holder.bind(listFacilities[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPFacilitiesViewHolder {
        val binding = ItemEventPdpFacilitiesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventPDPFacilitiesViewHolder(binding)
    }

    fun setList(list: List<Facilities>) {
        listFacilities = list
        notifyDataSetChanged()
    }
}
