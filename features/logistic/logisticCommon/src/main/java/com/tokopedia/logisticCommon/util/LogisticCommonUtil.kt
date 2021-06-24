package com.tokopedia.logisticCommon.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

object LogisticCommonUtil {

    const val ANA_REVAMP_ROLLENCE = "revamp_ana"

    /**
     * Rollence key
     */
    fun isRollOutUserANARevamp(): Boolean {
        val rollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(ANA_REVAMP_ROLLENCE, "")
        return rollenceValue == ANA_REVAMP_ROLLENCE
    }
}