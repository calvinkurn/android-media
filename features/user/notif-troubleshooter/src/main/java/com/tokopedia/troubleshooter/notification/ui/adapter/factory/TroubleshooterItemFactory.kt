package com.tokopedia.troubleshooter.notification.ui.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.troubleshooter.notification.ui.adapter.viewholder.*
import com.tokopedia.troubleshooter.notification.ui.listener.ConfigItemListener
import com.tokopedia.troubleshooter.notification.ui.uiview.*

class TroubleshooterItemFactory(
        private val listener: ConfigItemListener
): BaseAdapterTypeFactory(), TroubleshooterTypeFactory {

    override fun type(config: ConfigUIView): Int {
        return ConfigViewHolder.LAYOUT
    }

    override fun type(ticker: TickerUIView): Int {
        return TickerViewHolder.LAYOUT
    }

    override fun type(footer: FooterUIView): Int {
        return FooterViewHolder.LAYOUT
    }

    override fun type(status: StatusUIView): Int {
        return StatusViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            ConfigViewHolder.LAYOUT -> ConfigViewHolder(listener, parent)
            TickerViewHolder.LAYOUT -> TickerViewHolder(listener, parent)
            StatusViewHolder.LAYOUT -> StatusViewHolder(parent)
            FooterViewHolder.LAYOUT -> FooterViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}