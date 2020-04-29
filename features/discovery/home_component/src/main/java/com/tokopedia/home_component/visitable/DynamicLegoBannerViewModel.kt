package com.tokopedia.home_component.visitable

import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.DynamicChannelModel
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class DynamicLegoBannerViewModel(
        val channelModel: ChannelModel
): HomeComponentVisitable {
    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}