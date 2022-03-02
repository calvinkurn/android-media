package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.os.Bundle
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialHomeVisitable

class DynamicChannelDataModel(
        val dynamicChannelData: OfficialStoreChannel
) : OfficialHomeVisitable {

    fun getLayoutType() = when(dynamicChannelData.channel.layout) {
        DynamicChannelLayout.LAYOUT_SPRINT_LEGO -> DynamicChannelSprintSaleViewHolder.LAYOUT
        else -> DynamicChannelThematicViewHolder.LAYOUT
    }

    override fun visitableId(): String? = dynamicChannelData.channel.id

    override fun equalsWith(b: Any?): Boolean = b is DynamicChannelDataModel && b.dynamicChannelData == dynamicChannelData

    override fun getChangePayloadFrom(b: Any?): Bundle? = null

    override fun type(typeFactory: OfficialHomeTypeFactory): Int = typeFactory.type(this)
}
