package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.view.View
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentOneDataModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.ComponentOneViewHolder

class DiscoveryHomeFactoryImpl : DiscoveryHomeFactory {

    override fun type(componentOneDataModel: ComponentOneDataModel): Int {
        return ComponentOneViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            ComponentOneViewHolder.LAYOUT -> ComponentOneViewHolder(view)
            else -> throw RuntimeException("Layout Not Found")
        }
    }
}