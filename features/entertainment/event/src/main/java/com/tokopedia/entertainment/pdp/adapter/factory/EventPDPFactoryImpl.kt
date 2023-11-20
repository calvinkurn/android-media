package com.tokopedia.entertainment.pdp.adapter.factory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.databinding.PartialEventPdpAboutBinding
import com.tokopedia.entertainment.databinding.PartialEventPdpDescriptionBinding
import com.tokopedia.entertainment.databinding.PartialEventPdpDetailLokasiBinding
import com.tokopedia.entertainment.databinding.PartialEventPdpFacilitiesBinding
import com.tokopedia.entertainment.databinding.PartialEventPdpInformationBinding
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPAboutViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPFacilitiesViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPHighlightViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPInformationViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPLocationDetailViewHolder
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPAboutEntity
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPFacilitiesEntity
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPHighlightEntity
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPInformationEntity
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPLocationDetailEntity
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener

class EventPDPFactoryImpl(private val onBindItemListener: OnBindItemListener):BaseAdapterTypeFactory(), EventPDPFactory{

    override fun type(dataModel: EventPDPAboutEntity): Int = EventPDPAboutViewHolder.LAYOUT
    override fun type(dataModel: EventPDPHighlightEntity): Int = EventPDPHighlightViewHolder.LAYOUT
    override fun type(dataModel: EventPDPFacilitiesEntity): Int = EventPDPFacilitiesViewHolder.LAYOUT
    override fun type(dataModel: EventPDPLocationDetailEntity): Int = EventPDPLocationDetailViewHolder.LAYOUT
    override fun type(dataModel: EventPDPInformationEntity): Int = EventPDPInformationViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            EventPDPHighlightViewHolder.LAYOUT -> {
                val binding = PartialEventPdpDescriptionBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as? ViewGroup,
                    false
                )
                EventPDPHighlightViewHolder(binding, onBindItemListener)
            }
            EventPDPAboutViewHolder.LAYOUT -> {
                val binding = PartialEventPdpAboutBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as? ViewGroup,
                    false
                )
                EventPDPAboutViewHolder(binding, onBindItemListener)
            }
            EventPDPFacilitiesViewHolder.LAYOUT -> {
                val binding = PartialEventPdpFacilitiesBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as? ViewGroup,
                    false
                )
                EventPDPFacilitiesViewHolder(binding, onBindItemListener)
            }
            EventPDPLocationDetailViewHolder.LAYOUT -> {
                val binding = PartialEventPdpDetailLokasiBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as? ViewGroup,
                    false
                )
                EventPDPLocationDetailViewHolder(binding, onBindItemListener)
            }
            EventPDPInformationViewHolder.LAYOUT -> {
                val binding = PartialEventPdpInformationBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as? ViewGroup,
                    false
                )
                EventPDPInformationViewHolder(binding)
            }
            else -> super.createViewHolder(view, type)
        }
    }
}
