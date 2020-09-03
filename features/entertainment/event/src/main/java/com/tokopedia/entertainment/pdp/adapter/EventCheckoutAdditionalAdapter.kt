package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData
import com.tokopedia.entertainment.pdp.listener.OnAdditionalListener
import kotlinx.android.synthetic.main.item_checkout_event_data_tambahan_item.view.*
import kotlinx.android.synthetic.main.item_checkout_event_data_tambahan_package.view.*

class EventCheckoutAdditionalAdapter(private val listener: OnAdditionalListener) : RecyclerView.Adapter<EventCheckoutAdditionalAdapter.EventCheckoutAdditionalViewHolder>(){

    private var listAdditional = emptyList<EventCheckoutAdditionalData>()

    inner class EventCheckoutAdditionalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(additionalData: EventCheckoutAdditionalData) {
            with(itemView) {
                when(additionalData.additionalType.type){
                    1 -> {
                        item_checkout_event_additional_package.setOnClickListener {
                            clickedItem(additionalData)
                        }
                    }

                    2 -> {
                        item_checkout_event_additional_package.setOnClickListener {
                            clickedItem(additionalData)
                        }
                    }

                    3 -> {
                        tg_event_additional_item_title.text = additionalData.titleItem
                        item_checkout_event_additional_item.setOnClickListener {
                            clickedItem(additionalData)
                        }
                    }

                    4 -> {
                        item_checkout_event_additional_item.setOnClickListener {
                            clickedItem(additionalData)
                        }
                    }
                }
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

    private fun clickedItem(additionalData: EventCheckoutAdditionalData){
        listener.onClickAdditional(additionalData)
    }
}