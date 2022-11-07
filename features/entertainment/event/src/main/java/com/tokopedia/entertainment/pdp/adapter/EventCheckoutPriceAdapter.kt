package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse
import kotlinx.android.synthetic.main.item_event_checkout_price.view.*

class EventCheckoutPriceAdapter : RecyclerView.Adapter<EventCheckoutPriceAdapter.EventCheckoutPriceViewHolder>() {

    private val zeroPrice = 0L
    private var listItemMap = emptyList<ItemMapResponse>()

    inner class EventCheckoutPriceViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: ItemMapResponse) {
            with(itemView) {
                tg_event_checkout_summary_packet.text = item.name
                tg_event_checkout_summary_amount.text = resources.getString(R.string.ent_checkout_summary_amount,item.quantity)

                val result = item.price*item.quantity
                tg_event_checkout_summary_price.text = if(result != zeroPrice) getRupiahFormat(result) else resources.getString(R.string.ent_free_price)
            }
        }
    }

    override fun getItemCount(): Int = listItemMap.size
    override fun onBindViewHolder(holder: EventCheckoutPriceViewHolder, position: Int) {
        holder.bind(listItemMap[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventCheckoutPriceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event_checkout_price, parent, false)
        return EventCheckoutPriceViewHolder(itemView)
    }

    fun setList(list: List<ItemMapResponse>) {
        listItemMap = list
        notifyDataSetChanged()
    }
}