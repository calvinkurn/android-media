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
    private const val CONTROL_REVAMP_ATF = ""

    fun fetchAtfRollenceValue(context: Context) {
        rollenceAtfValue = try {
            val rollenceAtf = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_ATF)
            if (DeviceScreenInfo.isTablet(context) || rollenceAtf != RollenceKey.HOME_COMPONENT_ATF_2) {
                CONTROL_REVAMP_ATF
            } else {
                rollenceAtf
            }
        } catch (_: Exception) {
            CONTROL_REVAMP_ATF
        }
    }

    private fun getAtfRollenceValue(): String {
        return rollenceAtfValue
    }

    fun isUsingAtf2Variant(): Boolean {
        return getAtfRollenceValue() == RollenceKey.HOME_COMPONENT_ATF_2
    }
}
