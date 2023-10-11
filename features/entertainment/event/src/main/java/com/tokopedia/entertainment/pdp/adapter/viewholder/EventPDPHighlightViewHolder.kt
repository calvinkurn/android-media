package com.tokopedia.entertainment.pdp.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.PartialEventPdpDescriptionBinding
import com.tokopedia.entertainment.pdp.adapter.EventPDPHighlightAdapter
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPHighlightEntity
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class EventPDPHighlightViewHolder(val binding: PartialEventPdpDescriptionBinding, val onBindItemListener: OnBindItemListener) :
        AbstractViewHolder<EventPDPHighlightEntity>(binding.root) {

    var eventHighlightAdapter = EventPDPHighlightAdapter(onBindItemListener)

    override fun bind(element: EventPDPHighlightEntity) {
        with(binding) {
            if (element.isLoaded) {
                shimmering.root.gone()
                container.show()
                eventHighlightAdapter.setList(element.list)

                if (!element.title_small.isNullOrEmpty()) {
                    tgEventPdpTitleSmall.text = element.title_small
                } else {
                    tgEventPdpTitleSmall.gone()
                }

                if(!element.title_big.isNullOrEmpty()){
                    tgEventPdpTitleBig.text = element.title_big
                } else {
                    tgEventPdpTitleBig.gone()
                }

                if (!element.list.isNullOrEmpty()) {
                    rvEventPdpHighlight.let {
                        it.adapter = eventHighlightAdapter
                        it.layoutManager = LinearLayoutManager(
                                root.context,
                                RecyclerView.VERTICAL, false
                        )
                    }
                    onBindItemListener.performancePdp()
                } else {
                    rvEventPdpHighlight.gone()
                }


            } else {
                shimmering.root.show()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_event_pdp_description
    }
}
