package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPInformationAdapter
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPInformationEntity
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.partial_event_pdp_information.view.*

class EventPDPInformationViewHolder(view: View, val onBindItemListener: OnBindItemListener) :
        AbstractViewHolder<EventPDPInformationEntity>(view) {
    val eventPDPInformationAdapter = EventPDPInformationAdapter()
    override fun bind(element: EventPDPInformationEntity) {
        with(itemView) {
            if (element.isLoaded) {
                container.show()
                shimmering.gone()

                if (!element.sectionData.content.isNullOrEmpty()) {
                    if (!element.sectionData.content[0].valueAccordion.isNullOrEmpty()) {
                        eventPDPInformationAdapter.setList(element.sectionData.content[0].valueAccordion)
                        rv_event_pdp_information.apply {
                            adapter = eventPDPInformationAdapter
                            layoutManager = LinearLayoutManager(
                                    this@with.context,
                                    RecyclerView.VERTICAL, false
                            )
                        }
                    } else {
                        rv_event_pdp_information.gone()
                    }
                } else {
                    container.gone()
                    rv_event_pdp_information.gone()
                }
            } else {
                container.gone()
                shimmering.show()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_event_pdp_information
    }
}