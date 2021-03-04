package com.tokopedia.home.util

import com.tokopedia.home.HomeInternalRouter
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform


private const val ROLLANCE_EXP_NAME = AbTestPlatform.HOME_EXP
private const val ROLLANCE_VARIANT_OLD = AbTestPlatform.HOME_VARIANT_OLD
private const val ROLLANCE_VARIANT_REVAMP = AbTestPlatform.HOME_VARIANT_REVAMP

private fun isRollanceTestingUsingHomeRevamp(): Boolean {
    return try {
        val rollanceNavType = RemoteConfigInstance.getInstance().abTestPlatform.getString(ROLLANCE_EXP_NAME, ROLLANCE_VARIANT_OLD)
        rollanceNavType.equals(ROLLANCE_VARIANT_REVAMP, ignoreCase = true)
    } catch (e: Exception) {
        false
    }
}

fun isHomeRevampInstance(): Boolean {
    return isRollanceTestingUsingHomeRevamp()
}

fun isHomeRevampOld(): Boolean {
    return !isRollanceTestingUsingHomeRevamp()
}

fun homeRevampTestCondition(ifHomeRevamp: ()-> Unit = {}, ifHomeOld: ()-> Unit = {}) {
    if (isHomeRevampInstance()) {
        ifHomeRevamp.invoke()
    } else if (isHomeRevampOld()) {
        ifHomeOld.invoke()
    }
}