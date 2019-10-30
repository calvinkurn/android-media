package com.tokopedia.officialstore.official.presentation.dynamic_channel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.DynamicChannelLayoutType
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory

class DynamicChannelViewModel(
        val dynamicChannelData: Channel
) : Visitable<OfficialHomeAdapterTypeFactory> {

    override fun type(adapterTypeFactory: OfficialHomeAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

    fun getLayoutType() = when(dynamicChannelData.layout) {
        DynamicChannelLayoutType.LAYOUT_SPRINT_LEGO -> DynamicChannelSprintSaleViewHolder.LAYOUT
        DynamicChannelLayoutType.LAYOUT_6_IMAGE -> DynamicChannelLegoViewHolder.LAYOUT
        DynamicChannelLayoutType.LAYOUT_LEGO_3_IMAGE -> DynamicChannelLegoViewHolder.LAYOUT
        DynamicChannelLayoutType.LAYOUT_BANNER_CAROUSEL -> DynamicChannelThematicViewHolder.LAYOUT
        else -> DynamicChannelLegoViewHolder.LAYOUT
    }
}
