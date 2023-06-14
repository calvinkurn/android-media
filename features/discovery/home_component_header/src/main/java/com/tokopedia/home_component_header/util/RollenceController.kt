package com.tokopedia.home_component_header.util

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by Frenzel on 13/06/23
 */
internal object RollenceController {
    private var rollenceDynamicChannelHeader: String? = null

    fun isDynamicChannelHeaderUsingRollenceVariant(): Boolean {
        rollenceDynamicChannelHeader = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_DYNAMIC_CHANNEL_HEADER_EXP, "")
        return rollenceDynamicChannelHeader == RollenceKey.HOME_COMPONENT_DYNAMIC_CHANNEL_HEADER_VARIANT
    }
}
