package com.tokopedia.product.detail.common.pref

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object ProductRollenceHelper {
    fun rollenceAtcAnimationActive(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.PDP_ATC_ANIMATION_KEY,
            ""
        ) == RollenceKey.PDP_ATC_ANIMATION_VARIANT
    }
}
