package com.tokopedia.entertainment.pdp.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.*
import com.tokopedia.entertainment.pdp.data.pdp.*
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener

class EventPDPFactoryImpl(private val onBindItemListener: OnBindItemListener):BaseAdapterTypeFactory(), EventPDPFactory{

    override fun type(dataModel: EventPDPAboutEntity): Int = EventPDPAboutViewHolder.LAYOUT
    override fun type(dataModel: EventPDPHighlightEntity): Int = EventPDPHighlightViewHolder.LAYOUT
    override fun type(dataModel: EventPDPFacilitiesEntity): Int = EventPDPFacilitiesViewHolder.LAYOUT
    override fun type(dataModel: EventPDPLocationDetailEntity): Int = EventPDPLocationDetailViewHolder.LAYOUT
    override fun type(dataModel: EventPDPInformationEntity): Int = EventPDPInformationViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            EventPDPHighlightViewHolder.LAYOUT -> EventPDPHighlightViewHolder(view,onBindItemListener)
            EventPDPAboutViewHolder.LAYOUT -> EventPDPAboutViewHolder(view, onBindItemListener)
            EventPDPFacilitiesViewHolder.LAYOUT -> EventPDPFacilitiesViewHolder(view, onBindItemListener)
            EventPDPLocationDetailViewHolder.LAYOUT -> EventPDPLocationDetailViewHolder(view,onBindItemListener)
            EventPDPInformationViewHolder.LAYOUT -> EventPDPInformationViewHolder(view, onBindItemListener)
            else -> super.createViewHolder(view, type)
        }
    }
}