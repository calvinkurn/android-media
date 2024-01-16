package com.tokopedia.unifyorderhistory.util

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import timber.log.Timber

object UohRollenceUtil {

    fun isEnableAutoRedirectionToCartOnRepurchase(): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().
            abTestPlatform.getString(RollenceKey.UOH_REPURCHASE, RollenceKey.CONTROL_VARIANT)
            return (remoteConfigRollenceValue == RollenceKey.EXPERIMENT_VARIANT)
        } catch (e: Exception) {
            true
        }
    }

    fun isEnableBuyAgainWidget(): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.UOH_BUY_AGAIN_WIDGET, RollenceKey.UOH_BUY_AGAIN_WIDGET_CONTROL)
            return (remoteConfigRollenceValue == RollenceKey.UOH_BUY_AGAIN_WIDGET_VARIANT)
        } catch (e: Exception) {
            Timber.d(e.message)
            false
        }
    }
}
