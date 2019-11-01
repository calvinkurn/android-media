package com.tokopedia.officialstore.official.presentation.dynamic_channel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.DynamicChannelIdentifiers
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory

class DynamicChannelViewModel(
        val dynamicChannelData: Channel
) : Visitable<OfficialHomeAdapterTypeFactory> {

    override fun type(adapterTypeFactory: OfficialHomeAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

    fun getLayoutType() = when(dynamicChannelData.layout) {
        DynamicChannelIdentifiers.LAYOUT_SPRINT_LEGO -> DynamicChannelSprintSaleViewHolder.LAYOUT
        DynamicChannelIdentifiers.LAYOUT_6_IMAGE -> DynamicChannelLegoViewHolder.LAYOUT
        DynamicChannelIdentifiers.LAYOUT_LEGO_3_IMAGE -> DynamicChannelLegoViewHolder.LAYOUT
        DynamicChannelIdentifiers.LAYOUT_BANNER_CAROUSEL -> DynamicChannelThematicViewHolder.LAYOUT
        else -> DynamicChannelLegoViewHolder.LAYOUT
    }
}
