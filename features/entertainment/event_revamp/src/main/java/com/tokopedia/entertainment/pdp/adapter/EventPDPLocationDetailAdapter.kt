package com.tokopedia.entertainment.pdp.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.ValueBullet
import kotlinx.android.synthetic.main.item_event_pdp_detail_location.view.*

class EventPDPLocationDetailAdapter(): RecyclerView.Adapter<EventPDPLocationDetailAdapter.EventPDPLocationDetailViewHolder>() {

    private var listBullet = emptyList<ValueBullet>()

    inner class EventPDPLocationDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(valueBullet: ValueBullet) {
            with(itemView) {
                tg_event_pdp_how_to_get_there_number.text = valueBullet.bullet
                tg_event_pdp_how_to_get_there_desc.
                        setText(Html.fromHtml(resources.getString(R.string.ent_checkout_location_span, valueBullet.title,valueBullet.description)))
            }
        }
    }

    override fun getItemCount(): Int = listBullet.size
    override fun onBindViewHolder(holder: EventPDPLocationDetailViewHolder, position: Int) {
        holder.bind(listBullet[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPLocationDetailViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event_pdp_detail_location, parent, false)
        return EventPDPLocationDetailViewHolder(itemView)
    }

    fun setList(list: List<ValueBullet>) {
        listBullet = list
        notifyDataSetChanged()
    }
}