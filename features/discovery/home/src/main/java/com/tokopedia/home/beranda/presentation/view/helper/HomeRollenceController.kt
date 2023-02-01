package com.tokopedia.home.beranda.presentation.view.helper

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by frenzel on 09/05/22.
 */
object HomeRollenceController {
    private var rollenceAtfValue: String = ""

    fun fetchAtfRollenceValue() {
        rollenceAtfValue = try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_ATF)
        } catch (e: Exception) {
            ""
        }
    }

    private fun getAtfRollenceValue(): String {
        return rollenceAtfValue
    }

    fun isUsingAtf1Variant(): Boolean {
        return getAtfRollenceValue() == RollenceKey.HOME_COMPONENT_ATF_1
    }

    fun isUsingAtf2Variant(): Boolean {
        return getAtfRollenceValue() == RollenceKey.HOME_COMPONENT_ATF_2
    }
}
