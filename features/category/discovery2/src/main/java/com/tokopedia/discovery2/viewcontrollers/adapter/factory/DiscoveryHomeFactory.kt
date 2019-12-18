package com.tokopedia.discovery2.viewcontrollers.adapter.factory

import android.view.View
import com.tokopedia.discovery2.data.ComponentOneDataModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryVisitable
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.ComponentOneViewHolder

interface DiscoveryHomeFactory {
    fun createViewHolder(viewType: Int,view: View): AbstractViewHolder<*>?
    fun createDataModel(viewType: Int): DiscoveryVisitable?
}