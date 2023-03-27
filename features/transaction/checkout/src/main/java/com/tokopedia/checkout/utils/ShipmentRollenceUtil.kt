package com.tokopedia.checkout.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object ShipmentRollenceUtil {

    @JvmStatic
    fun isEnableRollenceCheckoutNewUpsellImprovement(): Boolean {
        return true // for testing
        try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance()
                .abTestPlatform.getString(RollenceKey.CHECKOUT_PLUS_NEW_UPSELL_IMPROVEMENT, RollenceKey.CONTROL_VARIANT)
            return (remoteConfigRollenceValue == RollenceKey.EXPERIMENT_VARIANT)
        } catch (e: Exception) {
            return false
        }
    }
}
