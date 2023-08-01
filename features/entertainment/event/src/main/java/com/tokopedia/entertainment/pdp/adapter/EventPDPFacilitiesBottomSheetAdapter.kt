package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.databinding.ItemEventPdpFacilitiesBottomSheetBinding
import com.tokopedia.entertainment.pdp.data.Facilities
import com.tokopedia.media.loader.loadImage

class EventPDPFacilitiesBottomSheetAdapter: RecyclerView.Adapter<EventPDPFacilitiesBottomSheetAdapter.EventPDPFacilitiesBottomSheetViewHolder>() {

    private var listOpenHour = emptyList<Facilities>()

    inner class EventPDPFacilitiesBottomSheetViewHolder(val binding: ItemEventPdpFacilitiesBottomSheetBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(facilities: Facilities) {
            with(binding) {
                ivEventPdpFacilitiesBottomSheet.loadImage(facilities.iconUrl)
                tgEventPdpFacilitiesBottomSheet.text = facilities.title
            }
        }
    }

    override fun getItemCount(): Int = listOpenHour.size
    override fun onBindViewHolder(holder: EventPDPFacilitiesBottomSheetViewHolder, position: Int) {
        holder.bind(listOpenHour[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPFacilitiesBottomSheetViewHolder {
        val binding = ItemEventPdpFacilitiesBottomSheetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventPDPFacilitiesBottomSheetViewHolder(binding)
    }

    fun setList(list: List<Facilities>) {
        listOpenHour = list
        notifyDataSetChanged()
    }
}
