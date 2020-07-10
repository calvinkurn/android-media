package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPHighlightAdapter
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPHighlightEntity
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.partial_event_pdp_description.view.*

class EventPDPHighlightViewHolder(view: View, val onBindItemListener: OnBindItemListener) :
        AbstractViewHolder<EventPDPHighlightEntity>(view) {

    var eventHighlightAdapter = EventPDPHighlightAdapter(onBindItemListener)

    override fun bind(element: EventPDPHighlightEntity) {
        with(itemView) {
            if (element.isLoaded) {
                shimmering.gone()
                container.show()
                eventHighlightAdapter.setList(element.list)

                if (!element.title_small.isNullOrEmpty()) {
                    tg_event_pdp_title_small.text = element.title_small
                } else {
                    tg_event_pdp_title_small.gone()
                }

                if(!element.title_big.isNullOrEmpty()){
                    tg_event_pdp_title_big.text = element.title_big
                } else {
                    tg_event_pdp_title_big.gone()
                }

                if (!element.list.isNullOrEmpty()) {
                    rv_event_pdp_highlight.apply {
                        adapter = eventHighlightAdapter
                        layoutManager = LinearLayoutManager(
                                this@with.context,
                                RecyclerView.VERTICAL, false
                        )
                    }
                    onBindItemListener.performancePdp()
                } else {
                    rv_event_pdp_highlight.gone()
                }


            } else {
                shimmering.show()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_event_pdp_description
    }
}