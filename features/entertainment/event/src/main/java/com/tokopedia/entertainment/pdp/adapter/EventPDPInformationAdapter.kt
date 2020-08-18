package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.ValueAccordion
import kotlinx.android.synthetic.main.item_event_pdp_information.view.*

class EventPDPInformationAdapter: RecyclerView.Adapter<EventPDPInformationAdapter.EventPDPInformationViewHolder>() {

    private var listAccordion = emptyList<ValueAccordion>()

    inner class EventPDPInformationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(valueAccordion: ValueAccordion) {
            with(itemView) {
                expand_event_pdp_information.setTitleText(valueAccordion.title)
                tg_event_pdp_information_title.text = valueAccordion.content
            }
        }
    }

    override fun getItemCount(): Int = listAccordion.size
    override fun onBindViewHolder(holder: EventPDPInformationViewHolder, position: Int) {
        holder.bind(listAccordion[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPInformationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event_pdp_information, parent, false)
        return EventPDPInformationViewHolder(itemView)
    }

    fun setList(list: List<ValueAccordion>) {
        listAccordion = list
        notifyDataSetChanged()
    }
}