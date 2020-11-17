package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter
import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.ent_pdp_form_chips_item.view.*
import kotlinx.android.synthetic.main.item_event_checkout_price.view.*

class EventFormChipsAdapter : RecyclerView.Adapter<EventFormChipsAdapter.EventFormChipsViewHolder>() {

    private var listChips = emptyList<String>()

    inner class EventFormChipsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: String, position: Int) {
            with(itemView) {
                chips_form.centerText = true
                chips_form.chipSize = ChipsUnify.SIZE_MEDIUM
                chips_form.chipText = item
                if (position > 0){
                    chips_form.setMargin(context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toPx().toInt(), 0, 0, 0)
                }
            }
        }
    }

    override fun getItemCount(): Int = listChips.size
    override fun onBindViewHolder(holder: EventFormChipsViewHolder, position: Int) {
        holder.bind(listChips[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventFormChipsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ent_pdp_form_chips_item, parent, false)
        return EventFormChipsViewHolder(itemView)
    }

    fun setList(list: List<String>) {
        listChips = list
        notifyDataSetChanged()
    }
}