package com.tokopedia.troubleshooter.notification.ui.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.troubleshooter.notification.ui.adapter.viewholder.ConfigViewHolder
import com.tokopedia.troubleshooter.notification.ui.adapter.viewholder.FooterViewHolder
import com.tokopedia.troubleshooter.notification.ui.adapter.viewholder.StatusViewHolder
import com.tokopedia.troubleshooter.notification.ui.adapter.viewholder.TickerViewHolder
import com.tokopedia.troubleshooter.notification.ui.listener.ConfigItemListener
import com.tokopedia.troubleshooter.notification.ui.listener.FooterListener
import com.tokopedia.troubleshooter.notification.ui.state.ConfigUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.FooterUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerUIView

class TroubleshooterItemFactory(
        private val itemListener: ConfigItemListener,
        private val footerListener: FooterListener
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
            FooterViewHolder.LAYOUT -> FooterViewHolder(footerListener, parent)
            ConfigViewHolder.LAYOUT -> ConfigViewHolder(itemListener, parent)
            TickerViewHolder.LAYOUT -> TickerViewHolder(parent)
            StatusViewHolder.LAYOUT -> StatusViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}