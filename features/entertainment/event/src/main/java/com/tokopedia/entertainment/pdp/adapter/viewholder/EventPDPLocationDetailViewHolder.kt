package com.tokopedia.entertainment.pdp.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.PartialEventPdpDetailLokasiBinding
import com.tokopedia.entertainment.pdp.adapter.EventPDPLocationDetailAdapter
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPLocationDetailEntity
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class EventPDPLocationDetailViewHolder(val binding: PartialEventPdpDetailLokasiBinding, val onBindItemListener: OnBindItemListener) :
        AbstractViewHolder<EventPDPLocationDetailEntity>(binding.root) {

    var eventPdpLocationAdapter = EventPDPLocationDetailAdapter()

    override fun bind(element: EventPDPLocationDetailEntity) {
        with(binding) {
            if (element.isLoaded) {
                container.show()
                shimmering.root.gone()

                if (!element.outlet.name.isNullOrEmpty()) {
                    widgetEventPdpMaps.setLocationTitle(element.outlet.name)
                    widgetEventPdpMaps.setOnClickListener {
                        onBindItemListener.seeLocationDetail(element.outlet)
                    }
                } else{
                    widgetEventPdpMaps.gone()
                }

                if (!element.sectionData.section.isNullOrEmpty()) {
                    tgEventPdpDetailLokasiSubTitle.text = element.sectionData.section
                }

                if (!element.sectionData.content.isNullOrEmpty()) {
                    if (!element.sectionData.content.first().valueBulletList.isNullOrEmpty()) {
                        if(element.sectionData.content.first().valueBulletList.size > MAX_VALUE) {
                            eventPdpLocationAdapter.setList(element.sectionData.content.first().valueBulletList.subList(0, MAX_VALUE))
                        }else {
                            eventPdpLocationAdapter.setList(element.sectionData.content.first().valueBulletList)
                        }

                        rvEventPdpDetailLokasi.let {
                            it.adapter = eventPdpLocationAdapter
                            it.layoutManager = LinearLayoutManager(
                                    root.context,
                                    RecyclerView.VERTICAL, false
                            )
                        }
                    } else {
                        rvEventPdpDetailLokasi.gone()
                    }

                    if (!element.sectionData.content.first().valueBulletList.isNullOrEmpty() && !element.sectionData.section.isNullOrEmpty()) {
                        btnEventPdpDetailLokasiSeeAll.setOnClickListener {
                            onBindItemListener.seeAllHowtoGoThere(element.sectionData.content.first().valueBulletList, element.sectionData.section)
                        }
                    }else{
                        btnEventPdpDetailLokasiSeeAll.gone()
                    }
                } else {
                    rvEventPdpDetailLokasi.gone()
                    btnEventPdpDetailLokasiSeeAll.gone()
                }
            } else {
                container.gone()
                shimmering.root.show()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_event_pdp_detail_lokasi
        const val MAX_VALUE = 3
    }
}
