package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.Facilities
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_event_pdp_facilities_bottom_sheet.view.*

class EventPDPFacilitiesBottomSheetAdapter: RecyclerView.Adapter<EventPDPFacilitiesBottomSheetAdapter.EventPDPFacilitiesBottomSheetViewHolder>() {

    private var listOpenHour = emptyList<Facilities>()

    inner class EventPDPFacilitiesBottomSheetViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(facilities: Facilities) {
            with(itemView) {
                iv_event_pdp_facilities_bottom_sheet.loadImage(facilities.iconUrl)
                tg_event_pdp_facilities_bottom_sheet.text = facilities.title
            }
        }
    }

    override fun getItemCount(): Int = listOpenHour.size
    override fun onBindViewHolder(holder: EventPDPFacilitiesBottomSheetViewHolder, position: Int) {
        holder.bind(listOpenHour[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPFacilitiesBottomSheetViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event_pdp_facilities_bottom_sheet, parent, false)
        return EventPDPFacilitiesBottomSheetViewHolder(itemView)
    }

    fun setList(list: List<Facilities>) {
        listOpenHour = list
        notifyDataSetChanged()
    }
}