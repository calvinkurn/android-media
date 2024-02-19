package com.tokopedia.discovery.common.reimagine

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object SearchRolloutUniverse {
    fun shouldShowRolloutUniverse(): Boolean {
        val rollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.SEARCH_ROLLOUT_UNIVERSE_V2,
            ""
        )
        return rollenceValue == RollenceKey.SEARCH_ROLLOUT_UNIVERSE_V2
    }
}
