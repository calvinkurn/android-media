package com.tokopedia.officialstore.official.presentation.dynamic_channel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.DynamicChannelLayoutType
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory

class DynamicChannelViewModel(
        private val dynamicChannel: Channel
) : Visitable<OfficialHomeAdapterTypeFactory> {

    override fun type(adapterTypeFactory: OfficialHomeAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

    fun getLayoutType() = when(dynamicChannel.layout) {
        DynamicChannelLayoutType.LAYOUT_SPRINT_LEGO -> DynamicChannelLegoViewHolder.LAYOUT
        else -> DynamicChannelThematicViewHolder.LAYOUT
    }
}
