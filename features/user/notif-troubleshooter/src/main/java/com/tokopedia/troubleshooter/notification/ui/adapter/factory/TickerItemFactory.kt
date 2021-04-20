package com.tokopedia.troubleshooter.notification.ui.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.troubleshooter.notification.ui.adapter.viewholder.TickerItemViewHolder
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerItemUIView

class TickerItemFactory: BaseAdapterTypeFactory(), TickerTypeFactory {

    override fun type(ticker: TickerItemUIView): Int {
        return TickerItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            TickerItemViewHolder.LAYOUT -> TickerItemViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}