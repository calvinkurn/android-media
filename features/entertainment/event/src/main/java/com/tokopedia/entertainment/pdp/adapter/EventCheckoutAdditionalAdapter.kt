package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.checkout.AdditionalType
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData
import com.tokopedia.entertainment.pdp.listener.OnAdditionalListener
import kotlinx.android.synthetic.main.item_checkout_event_data_tambahan_item_filled.view.*
import kotlinx.android.synthetic.main.item_checkout_event_data_tambahan_item.view.*

class EventCheckoutAdditionalAdapter(private val listener: OnAdditionalListener) : RecyclerView.Adapter<EventCheckoutAdditionalAdapter.EventCheckoutAdditionalViewHolder>() {

    private var listAdditional = mutableListOf<EventCheckoutAdditionalData>()

    inner class EventCheckoutAdditionalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(additionalData: EventCheckoutAdditionalData) {
            additionalData.position = position
            with(itemView) {
                when (additionalData.additionalType.type) {
                    AdditionalType.ITEM_UNFILL.type -> {
                        tg_event_additional_item_title.text = additionalData.titleItem
                        item_checkout_event_additional_item.setOnClickListener {
                            clickedItem(additionalData)
                        }
                    }

                    AdditionalType.ITEM_FILLED.type -> {
                        val eventCheckoutAdditonalFilledAdapter = EventCheckoutAdditonalFilledAdapter()
                        eventCheckoutAdditonalFilledAdapter.setList(additionalData.listForm)
                        rv_event_checkout_additional_item_filled.apply {
                            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                            adapter = eventCheckoutAdditonalFilledAdapter
                        }
                        container_event_checkout_additional_item_filled.setOnClickListener {
                            clickedItem(additionalData)
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: EventCheckoutAdditionalViewHolder, position: Int) {
        holder.bind(listAdditional[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventCheckoutAdditionalViewHolder {
        val itemView = when (viewType) {
            AdditionalType.ITEM_UNFILL.type -> LayoutInflater.from(parent.context).inflate(R.layout.item_checkout_event_data_tambahan_item, parent, false)
            AdditionalType.ITEM_FILLED.type -> LayoutInflater.from(parent.context).inflate(R.layout.item_checkout_event_data_tambahan_item_filled, parent, false)
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

    fun setList(list: MutableList<EventCheckoutAdditionalData>) {
        listAdditional = list
        notifyDataSetChanged()
    }

    private fun clickedItem(additionalData: EventCheckoutAdditionalData) {
        listener.onClickAdditional(additionalData)
    }
}