package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPFacilitiesAdapter
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPFacilitiesEntity
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.partial_event_pdp_facilities.view.*

class EventPDPFacilitiesViewHolder(view: View, val onBindItemListener: OnBindItemListener)
    : AbstractViewHolder<EventPDPFacilitiesEntity>(view) {

    var eventFacilitiestAdapter = EventPDPFacilitiesAdapter()

    override fun bind(element: EventPDPFacilitiesEntity) {
        with(itemView) {
            if (element.isLoaded) {
                container.show()
                shimmering.gone()

                if(!element.list.isNullOrEmpty()) {
                    if(element.list.size > MAX_SIZE) {
                        eventFacilitiestAdapter.setList(element.list.subList(0, MAX_SIZE))
                    } else {
                        eventFacilitiestAdapter.setList(element.list)
                    }

                    rv_event_pdp_facilities.apply {
                        adapter = eventFacilitiestAdapter
                        layoutManager = GridLayoutManager(
                                this@with.context, 2
                        )
                    }
                    tg_event_pdp_facilities_see_all.setOnClickListener {
                        onBindItemListener.seeAllFacilities(element.list, resources.getString(R.string.ent_pdp_facilities))
                    }
                } else {
                    tg_event_pdp_facilities_title.gone()
                    rv_event_pdp_facilities.gone()
                    tg_event_pdp_facilities_see_all.gone()
                }

            } else {
                shimmering.show()
                container.gone()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_event_pdp_facilities
        const val MAX_SIZE = 4
    }
}