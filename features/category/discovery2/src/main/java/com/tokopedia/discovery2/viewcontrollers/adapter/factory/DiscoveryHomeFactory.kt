package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.view.View
import com.tokopedia.discovery2.data.ComponentOneDataModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.ComponentOneViewHolder

interface DiscoveryHomeFactory {
    fun type(componentOneDataModel: ComponentOneDataModel): Int
    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}