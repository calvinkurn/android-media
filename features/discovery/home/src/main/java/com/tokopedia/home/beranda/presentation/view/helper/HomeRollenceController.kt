package com.tokopedia.home.beranda.presentation.view.helper

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by frenzel on 09/05/22.
 */
object HomeRollenceController {
    var rollenceAtfValue: String = ""
    private const val EMPTY_VALUE = ""

    var rollenceLoadTime: String = ""

    fun fetchHomeRollenceValue(context: Context) {
        fetchAtfRollenceValue(context)
        fetchLoadTimeRollenceValue()
    }

    private fun fetchAtfRollenceValue(context: Context) {
        rollenceAtfValue = try {
            val rollenceAtf = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_ATF)
            if (DeviceScreenInfo.isTablet(context) || rollenceAtf != RollenceKey.HOME_COMPONENT_ATF_2) {
                EMPTY_VALUE
            } else {
                rollenceAtf
            }
        } catch (_: Exception) {
            EMPTY_VALUE
        }
    }

    private fun fetchLoadTimeRollenceValue() {
        rollenceLoadTime = try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.HOME_LOAD_TIME_KEY,
                RollenceKey.HOME_LOAD_TIME_CONTROL
            )
        } catch (_: Exception) {
            RollenceKey.HOME_LOAD_TIME_CONTROL
        }
    }

    private fun getAtfRollenceValue(): String {
        return rollenceAtfValue
    }

    fun isUsingAtf2Variant(): Boolean {
        return getAtfRollenceValue() == RollenceKey.HOME_COMPONENT_ATF_2
    }
}
