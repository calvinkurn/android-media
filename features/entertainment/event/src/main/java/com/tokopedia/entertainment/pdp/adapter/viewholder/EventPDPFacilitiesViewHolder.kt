package com.tokopedia.entertainment.pdp.adapter.viewholder

import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.PartialEventPdpFacilitiesBinding
import com.tokopedia.entertainment.pdp.adapter.EventPDPFacilitiesAdapter
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPFacilitiesEntity
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class EventPDPFacilitiesViewHolder(val binding: PartialEventPdpFacilitiesBinding, val onBindItemListener: OnBindItemListener)
    : AbstractViewHolder<EventPDPFacilitiesEntity>(binding.root) {

    var eventFacilitiestAdapter = EventPDPFacilitiesAdapter()

    override fun bind(element: EventPDPFacilitiesEntity) {
        with(binding) {
            if (element.isLoaded) {
                container.show()
                shimmering.root.gone()

                if(!element.list.isNullOrEmpty()) {
                    if(element.list.size > MAX_SIZE) {
                        eventFacilitiestAdapter.setList(element.list.subList(0, MAX_SIZE))
                    } else {
                        eventFacilitiestAdapter.setList(element.list)
                    }

                    rvEventPdpFacilities.let {
                        it.adapter = eventFacilitiestAdapter
                        it.layoutManager = GridLayoutManager(
                                root.context, GRID
                        )
                    }
                    tgEventPdpFacilitiesSeeAll.setOnClickListener {
                        onBindItemListener.seeAllFacilities(element.list, getString(R.string.ent_pdp_facilities))
                    }
                } else {
                    tgEventPdpFacilitiesTitle.gone()
                    rvEventPdpFacilities.gone()
                    tgEventPdpFacilitiesSeeAll.gone()
                }

            } else {
                shimmering.root.show()
                container.gone()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_event_pdp_facilities
        const val MAX_SIZE = 4
        const val GRID = 2
    }
}
