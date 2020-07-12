package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPLocationDetailAdapter
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPLocationDetailEntity
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.partial_event_pdp_detail_lokasi.view.*

class EventPDPLocationDetailViewHolder(view: View, val onBindItemListener: OnBindItemListener) :
        AbstractViewHolder<EventPDPLocationDetailEntity>(view) {

    var eventPdpLocationAdapter = EventPDPLocationDetailAdapter()

    override fun bind(element: EventPDPLocationDetailEntity) {
        with(itemView) {
            if (element.isLoaded) {
                container.show()
                shimmering.gone()

                if (!element.outlet.name.isNullOrEmpty()) {
                    widget_event_pdp_maps.setLocationTitle(element.outlet.name)
                    widget_event_pdp_maps.setOnClickListener {
                        onBindItemListener.seeLocationDetail(element.outlet)
                    }
                } else{
                    widget_event_pdp_maps.gone()
                }

                if (!element.sectionData.section.isNullOrEmpty()) {
                    tg_event_pdp_detail_lokasi_sub_title.text = element.sectionData.section
                }

                if (!element.sectionData.content.isNullOrEmpty()) {
                    if (!element.sectionData.content.first().valueBulletList.isNullOrEmpty()) {
                        if(element.sectionData.content.first().valueBulletList.size > MAX_VALUE) {
                            eventPdpLocationAdapter.setList(element.sectionData.content.first().valueBulletList.subList(0, MAX_VALUE))
                        }else {
                            eventPdpLocationAdapter.setList(element.sectionData.content.first().valueBulletList)
                        }

                        rv_event_pdp_detail_lokasi.apply {
                            adapter = eventPdpLocationAdapter
                            layoutManager = LinearLayoutManager(
                                    this@with.context,
                                    RecyclerView.VERTICAL, false
                            )
                        }
                    } else {
                        rv_event_pdp_detail_lokasi.gone()
                    }

                    if (!element.sectionData.content.first().valueBulletList.isNullOrEmpty() && !element.sectionData.section.isNullOrEmpty()) {
                        btn_event_pdp_detail_lokasi_see_all.setOnClickListener {
                            onBindItemListener.seeAllHowtoGoThere(element.sectionData.content.first().valueBulletList, element.sectionData.section)
                        }
                    }else{
                        btn_event_pdp_detail_lokasi_see_all.gone()
                    }
                } else {
                    rv_event_pdp_detail_lokasi.gone()
                    btn_event_pdp_detail_lokasi_see_all.gone()
                }
            } else {
                container.gone()
                shimmering.show()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_event_pdp_detail_lokasi
        const val MAX_VALUE = 3
    }
}