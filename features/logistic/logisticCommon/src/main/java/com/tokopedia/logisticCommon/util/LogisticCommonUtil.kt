package com.tokopedia.logisticCommon.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

object LogisticCommonUtil {

    const val ANA_REVAMP_ROLLENCE_KEY = "revamp_ana"
    const val ANA_REVAMP_VARIANT = "ana_revamp"

    /**
     * Rollence key
     */
    fun isRollOutUserANARevamp(): Boolean {
        val rollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(ANA_REVAMP_ROLLENCE_KEY, "")
        return rollenceValue == ANA_REVAMP_VARIANT
    }
}