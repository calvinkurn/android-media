package com.tokopedia.troubleshooter.notification.ui.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.troubleshooter.notification.ui.adapter.viewholder.ConfigViewHolder
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigUIView

class TroubleshooterItemFactory: BaseAdapterTypeFactory(), TroubleshooterTypeFactory {

    override fun type(config: ConfigUIView): Int {
        return ConfigViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            ConfigViewHolder.LAYOUT -> ConfigViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}