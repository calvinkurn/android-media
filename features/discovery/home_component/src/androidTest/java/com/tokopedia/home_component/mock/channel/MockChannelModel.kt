package com.tokopedia.home_component.mock.channel

import com.tokopedia.home_component.model.ChannelModel

object MockChannelModel {
    fun get(layoutName: String): ChannelModel {
        return ChannelModel(
                "1",
                "11",
                "type",
                0,
                0,
                "",
                "",
                MockChannelHeader.get(),
                MockChannelBanner.get(),
                MockChannelConfig.get(layoutName),
                MockChannelTrackingAttribution.get(),
                MockChannelGrid.get()
        )
    }
}