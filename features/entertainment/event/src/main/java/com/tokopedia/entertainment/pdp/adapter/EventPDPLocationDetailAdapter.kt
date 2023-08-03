package com.tokopedia.entertainment.pdp.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.ItemEventPdpDetailLocationBinding
import com.tokopedia.entertainment.pdp.data.ValueBullet

class EventPDPLocationDetailAdapter: RecyclerView.Adapter<EventPDPLocationDetailAdapter.EventPDPLocationDetailViewHolder>() {

    private var listBullet = emptyList<ValueBullet>()

    inner class EventPDPLocationDetailViewHolder(val binding: ItemEventPdpDetailLocationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(valueBullet: ValueBullet) {
            with(binding) {
                tgEventPdpHowToGetThereNumber.text = valueBullet.bullet
                tgEventPdpHowToGetThereDesc.
                        setText(Html.fromHtml(root.context.getString(R.string.ent_checkout_location_span, valueBullet.title,valueBullet.description)))
            }
        }
    }

    override fun getItemCount(): Int = listBullet.size
    override fun onBindViewHolder(holder: EventPDPLocationDetailViewHolder, position: Int) {
        holder.bind(listBullet[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPLocationDetailViewHolder {
        val binding = ItemEventPdpDetailLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventPDPLocationDetailViewHolder(binding)
    }

    fun setList(list: List<ValueBullet>) {
        listBullet = list
        notifyDataSetChanged()
    }
}
