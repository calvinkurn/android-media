package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tokopedia.entertainment.databinding.ItemCheckoutEventDataTambahanItemBinding
import com.tokopedia.entertainment.databinding.ItemCheckoutEventDataTambahanItemFilledBinding
import com.tokopedia.entertainment.pdp.data.checkout.AdditionalType
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData
import com.tokopedia.entertainment.pdp.listener.OnAdditionalListener

class EventCheckoutAdditionalAdapter(private val listener: OnAdditionalListener) : RecyclerView.Adapter<EventCheckoutAdditionalAdapter.EventCheckoutAdditionalViewHolder>() {

    private var listAdditional = mutableListOf<EventCheckoutAdditionalData>()

    inner class EventCheckoutAdditionalViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(additionalData: EventCheckoutAdditionalData) {
            additionalData.position = position
            with(itemView) {
                when (additionalData.additionalType.type) {
                    AdditionalType.ITEM_UNFILL.type -> {
                        (binding as ItemCheckoutEventDataTambahanItemBinding).tgEventAdditionalItemTitle.text = additionalData.titleItem
                        (binding as ItemCheckoutEventDataTambahanItemBinding).itemCheckoutEventAdditionalItem.setOnClickListener {
                            clickedItem(additionalData)
                        }
                    }

                    AdditionalType.ITEM_FILLED.type -> {
                        val eventCheckoutAdditonalFilledAdapter = EventCheckoutAdditonalFilledAdapter()
                        eventCheckoutAdditonalFilledAdapter.setList(additionalData.listForm)
                        (binding as ItemCheckoutEventDataTambahanItemFilledBinding).rvEventCheckoutAdditionalItemFilled.apply {
                            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                            adapter = eventCheckoutAdditonalFilledAdapter
                        }
                        (binding as ItemCheckoutEventDataTambahanItemFilledBinding).containerEventCheckoutAdditionalItemFilled.setOnClickListener {
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
        val binding = when (viewType) {
            AdditionalType.ITEM_UNFILL.type -> ItemCheckoutEventDataTambahanItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            AdditionalType.ITEM_FILLED.type -> ItemCheckoutEventDataTambahanItemFilledBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            else -> ItemCheckoutEventDataTambahanItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        }

        return EventCheckoutAdditionalViewHolder(binding)
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
