package com.tokopedia.entertainment.pdp.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPTicketBannerViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.PackageParentViewHolder
import com.tokopedia.entertainment.pdp.data.EventPDPTicketBanner
import com.tokopedia.entertainment.pdp.data.EventPDPTicketGroup
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener

class PackageTypeFactoryImpl(private val onBindItemTicketListener: OnBindItemTicketListener ): BaseAdapterTypeFactory(), PackageTypeFactory {
    

    override fun type(dataModel: EventPDPTicketGroup): Int {
        return PackageParentViewHolder.LAYOUT
    }

    override fun type(dataModel: EventPDPTicketBanner): Int {
        return EventPDPTicketBannerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            PackageParentViewHolder.LAYOUT -> PackageParentViewHolder(view, onBindItemTicketListener)
            EventPDPTicketBannerViewHolder.LAYOUT -> EventPDPTicketBannerViewHolder(view)
            else -> super.createViewHolder(view, type)
        }

    }
}
