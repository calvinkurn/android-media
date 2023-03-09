package com.tokopedia.home.beranda.presentation.view.helper

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by frenzel on 09/05/22.
 */
object HomeRollenceController {
    var rollenceAtfValue: String = ""
    const val CONTROL_REVAMP_ATF = ""

    fun fetchAtfRollenceValue() {
        rollenceAtfValue = try {
            val rollenceAtf = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_ATF)
            if (rollenceAtf == RollenceKey.HOME_COMPONENT_ATF_2) rollenceAtf else CONTROL_REVAMP_ATF
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
