package com.tokopedia.home_component.mock.channel

import com.tokopedia.home_component.model.ChannelBanner
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel

object MockChannelConfig {
    fun get(layoutName: String): ChannelConfig {
        return ChannelConfig(
                layoutName,
                showPromoBadge = true,
                hasCloseButton = true,
                serverTimeOffset = 0,
                createdTimeMillis = "",
                isAutoRefreshAfterExpired = false
        )
    }
}