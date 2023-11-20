package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.databinding.ItemEventPdpHighlightBinding
import com.tokopedia.entertainment.pdp.data.pdp.Highlight
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventPDPOpenHourMapper.openHourList
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventPDPOpenHourMapper.openHourMapperTitle
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show

class EventPDPHighlightAdapter(val onBindItemListener: OnBindItemListener): RecyclerView.Adapter<EventPDPHighlightAdapter.EventPDPHighlightViewHolder>(){

    private var listCategories = emptyList<Highlight>()

    inner class EventPDPHighlightViewHolder(val binding: ItemEventPdpHighlightBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(highlight: Highlight) {
            with(binding) {
                ivEventPdpHighlight.loadImage(highlight.icon)
                tgEventPdpHighlightTitle.text = highlight.title

                if(highlight.title.equals(OPEN_HOUR, true)){
                    tgEventPdpHighlightDesc.text = openHourMapperTitle(highlight.description)
                    tgEventPdpHighlightSeeAll.show()
                    viewEventPdpHighlightLine.show()
                    tgEventPdpHighlightSeeAll.setOnClickListener {
                        onBindItemListener.seeAllOpenHour(openHourList(highlight.description),highlight.title)
                    }

                } else{
                    tgEventPdpHighlightDesc.text = highlight.description
                }
            }
        }
    }

    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: EventPDPHighlightViewHolder, position: Int) {
        holder.bind(listCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPHighlightViewHolder {
        val binding = ItemEventPdpHighlightBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventPDPHighlightViewHolder(binding)
    }

    fun setList(list: List<Highlight>) {
        listCategories = list
        notifyDataSetChanged()
    }

    companion object{
        const val OPEN_HOUR = "Jam Buka"
    }
}
