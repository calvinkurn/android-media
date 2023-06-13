package com.tokopedia.unifyorderhistory.util

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object UohRollenceUtil {

    fun isEnableAutoRedirectionToCartOnRepurchase(): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.UOH_REPURCHASE, RollenceKey.CONTROL_VARIANT)
            return (remoteConfigRollenceValue == RollenceKey.EXPERIMENT_VARIANT)
        } catch (e: Exception) {
            true
        }
    }
}
