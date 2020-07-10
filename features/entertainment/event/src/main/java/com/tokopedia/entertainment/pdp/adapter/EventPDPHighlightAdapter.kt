package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.pdp.Highlight
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventPDPOpenHourMapper.openHourList
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventPDPOpenHourMapper.openHourMapperTitle
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_event_pdp_highlight.view.*

class EventPDPHighlightAdapter(val onBindItemListener: OnBindItemListener): RecyclerView.Adapter<EventPDPHighlightAdapter.EventPDPHighlightViewHolder>(){

    private var listCategories = emptyList<Highlight>()

    inner class EventPDPHighlightViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(highlight: Highlight) {
            with(itemView) {
                iv_event_pdp_highlight.loadImage(highlight.icon)
                tg_event_pdp_highlight_title.text = highlight.title

                if(highlight.title.equals(OPEN_HOUR, true)){
                    tg_event_pdp_highlight_desc.text = openHourMapperTitle(highlight.description)
                    tg_event_pdp_highlight_see_all.show()
                    view_event_pdp_highlight_line.show()
                    tg_event_pdp_highlight_see_all.setOnClickListener {
                        onBindItemListener.seeAllOpenHour(openHourList(highlight.description),highlight.title)
                    }

                } else{
                    tg_event_pdp_highlight_desc.text = highlight.description
                }
            }
        }
    }

    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: EventPDPHighlightViewHolder, position: Int) {
        holder.bind(listCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPHighlightViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event_pdp_highlight, parent, false)
        return EventPDPHighlightViewHolder(itemView)
    }

    fun setList(list: List<Highlight>) {
        listCategories = list
        notifyDataSetChanged()
    }

    companion object{
        const val OPEN_HOUR = "Jam Buka"
    }
}