package com.tokopedia.home_component.util

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RollenceKey

object HomeComponentFeatureFlag {
    var missionExpVariant = RollenceKey.HOME_MISSION_SIZE_CONTROL

    fun isUsingNewLegoTracking(remoteConfig: RemoteConfig): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.HOME_NEW_LEGO_TRACKING, true)
    }

    fun fetchMissionRollenceValue() {
        missionExpVariant =  RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.HOME_MISSION_SIZE_KEY,
            RollenceKey.HOME_MISSION_SIZE_CONTROL
        )
    }

    fun isMissionExpVariant(): Boolean {
        return missionExpVariant == RollenceKey.HOME_MISSION_SIZE_VARIANT
    }
}
