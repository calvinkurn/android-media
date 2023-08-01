package com.tokopedia.home_component.util

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

object HomeComponentRemoteConfigController {
    fun isUsingNewLegoTracking(remoteConfig: RemoteConfig): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.HOME_NEW_LEGO_TRACKING, true)
    }
}
