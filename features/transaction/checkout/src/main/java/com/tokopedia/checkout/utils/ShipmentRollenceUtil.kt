package com.tokopedia.checkout.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object ShipmentRollenceUtil {

    @JvmStatic
    fun enableCheckoutNewUpsellImprovement(): Boolean {
        //return true // for testing
        try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance()
                .abTestPlatform.getString(RollenceKey.CHECKOUT_PLUS_NEW_UPSELL_IMPROVEMENT, getDefaultRollenceValue())
            return (remoteConfigRollenceValue == getVariantRollenceValue())
        } catch (e: Exception) {
            return false
        }
    }

    @JvmStatic
    fun getDefaultRollenceValue(): String {
        return RollenceKey.CONTROL_VARIANT
    }

    @JvmStatic
    fun getVariantRollenceValue(): String {
        return RollenceKey.EXPERIMENT_VARIANT
    }
}
