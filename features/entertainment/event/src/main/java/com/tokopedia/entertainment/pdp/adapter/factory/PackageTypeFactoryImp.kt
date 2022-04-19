package com.tokopedia.entertainment.pdp.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPTicketBannerViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.PackageParentViewHolder
import com.tokopedia.entertainment.pdp.data.EventPDPTicketBanner
import com.tokopedia.entertainment.pdp.data.EventPDPTicketGroup
import com.tokopedia.entertainment.pdp.data.PackageV3
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener
import com.tokopedia.entertainment.pdp.listener.OnCoachmarkListener

class PackageTypeFactoryImpl(private val onBindItemTicketListener: OnBindItemTicketListener,
                            private val onCoachmarkListener: OnCoachmarkListener): BaseAdapterTypeFactory(), PackageTypeFactory {
    

    override fun type(dataModel: EventPDPTicketGroup): Int {
        return PackageParentViewHolder.LAYOUT
    }

    override fun type(dataModel: EventPDPTicketBanner): Int {
        return EventPDPTicketBannerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            PackageParentViewHolder.LAYOUT -> PackageParentViewHolder(view, onBindItemTicketListener, onCoachmarkListener)
            EventPDPTicketBannerViewHolder.LAYOUT -> EventPDPTicketBannerViewHolder(view)
            else -> super.createViewHolder(view, type)
        }

    }
}