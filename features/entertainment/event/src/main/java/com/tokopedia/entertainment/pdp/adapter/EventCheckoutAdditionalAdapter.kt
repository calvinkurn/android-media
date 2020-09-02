package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData

class EventCheckoutAdditionalAdapter : RecyclerView.Adapter<EventCheckoutAdditionalAdapter.EventCheckoutAdditionalViewHolder>(){

    private var listAdditional = emptyList<EventCheckoutAdditionalData>()

    inner class EventCheckoutAdditionalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(valueAccordion: EventCheckoutAdditionalData) {
            with(itemView) {
            }
        }
    }

    override fun onBindViewHolder(holder: EventCheckoutAdditionalViewHolder, position: Int) {
            holder.bind(listAdditional[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventCheckoutAdditionalViewHolder {
        val itemView = when (viewType){
            1,2 -> LayoutInflater.from(parent.context).inflate(R.layout.item_checkout_event_data_tambahan_package, parent, false)
            3,4 -> LayoutInflater.from(parent.context).inflate(R.layout.item_checkout_event_data_tambahan_item, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.item_checkout_event_data_tambahan_package, parent, false)
        }

        return EventCheckoutAdditionalViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listAdditional.size
    }

    override fun getItemViewType(position: Int): Int {
        return listAdditional.get(position).additionalType.type
    }

    fun setList(list: List<EventCheckoutAdditionalData>) {
        listAdditional = list
        notifyDataSetChanged()
    }
}