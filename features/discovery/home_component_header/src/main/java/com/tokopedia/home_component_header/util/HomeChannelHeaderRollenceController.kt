package com.tokopedia.home_component_header.util

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by Frenzel on 13/06/23
 */
object HomeChannelHeaderRollenceController {
    private var rollenceDynamicChannelHeader: String = ""

    fun fetchHomeHeaderRollence(): String {
        rollenceDynamicChannelHeader = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_DYNAMIC_CHANNEL_HEADER_EXP, "")
        return rollenceDynamicChannelHeader
    }

    fun isHeaderUsingRollenceVariant(): Boolean {
        return rollenceDynamicChannelHeader == RollenceKey.HOME_COMPONENT_DYNAMIC_CHANNEL_HEADER_VARIANT
    }
}
