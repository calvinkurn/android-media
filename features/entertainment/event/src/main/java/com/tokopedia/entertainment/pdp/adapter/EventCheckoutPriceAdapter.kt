package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.ItemEventCheckoutPriceBinding
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse

class EventCheckoutPriceAdapter : RecyclerView.Adapter<EventCheckoutPriceAdapter.EventCheckoutPriceViewHolder>() {

    private val zeroPrice = 0L
    private var listItemMap = emptyList<ItemMapResponse>()

    inner class EventCheckoutPriceViewHolder(val binding: ItemEventCheckoutPriceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemMapResponse) {
            with(binding) {
                binding.tgEventCheckoutSummaryPacket.text = item.name
                binding.tgEventCheckoutSummaryAmount.text = root.context.resources.getString(R.string.ent_checkout_summary_amount,item.quantity)

                val result = item.price*item.quantity
                binding.tgEventCheckoutSummaryPrice.text = if(result != zeroPrice) getRupiahFormat(result) else root.context.resources.getString(R.string.ent_free_price)
            }
        }
    }

    override fun getItemCount(): Int = listItemMap.size
    override fun onBindViewHolder(holder: EventCheckoutPriceViewHolder, position: Int) {
        holder.bind(listItemMap[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventCheckoutPriceViewHolder {
        val binding = ItemEventCheckoutPriceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventCheckoutPriceViewHolder(binding)
    }

    fun setList(list: List<ItemMapResponse>) {
        listItemMap = list
        notifyDataSetChanged()
    }
}
