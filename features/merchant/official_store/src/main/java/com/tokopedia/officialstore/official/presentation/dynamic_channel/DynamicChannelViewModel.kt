package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.os.Bundle
import com.tokopedia.officialstore.DynamicChannelIdentifiers
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialHomeVisitable

class DynamicChannelViewModel(
        val dynamicChannelData: Channel
) : OfficialHomeVisitable {

    fun getLayoutType() = when(dynamicChannelData.layout) {
        DynamicChannelIdentifiers.LAYOUT_SPRINT_LEGO -> DynamicChannelSprintSaleViewHolder.LAYOUT
        DynamicChannelIdentifiers.LAYOUT_BANNER_CAROUSEL -> DynamicChannelThematicViewHolder.LAYOUT
        DynamicChannelIdentifiers.LAYOUT_MIX_LEFT -> DynamicChannelMixLeftViewHolder.LAYOUT
        DynamicChannelIdentifiers.LAYOUT_MIX_TOP -> DynamicChannelMixTopViewHolder.LAYOUT
        else -> DynamicChannelLegoViewHolder.LAYOUT
    }

    override fun visitableId(): String? = dynamicChannelData.id

    override fun equalsWith(b: Any?): Boolean = b is DynamicChannelViewModel && b.dynamicChannelData == dynamicChannelData

    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)
}
