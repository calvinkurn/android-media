package com.tokopedia.navigation.util

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object IconJumperUtil {

    @JvmStatic
    fun isEnabledIconJumper(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.ICON_JUMPER,
            RollenceKey.ICON_JUMPER_DEFAULT
        ) == RollenceKey.ICON_JUMPER_EXP
    }
}
