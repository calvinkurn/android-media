package com.tokopedia.checkout.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import timber.log.Timber

object ShipmentRollenceUtil {

    @JvmStatic
    fun enableCheckoutNewUpsellImprovement(): Boolean {
        try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance()
                .abTestPlatform.getString(
                    RollenceKey.CHECKOUT_PLUS_NEW_UPSELL_IMPROVEMENT,
                    getDefaultRollenceValueForNewUpsellImprovement()
                )
            return (remoteConfigRollenceValue == getVariantRollenceValueForNewUpsellImprovement())
        } catch (e: Exception) {
            Timber.e(e)
            return false
        }
    }

    private fun getDefaultRollenceValueForNewUpsellImprovement(): String {
        return RollenceKey.CONTROL_VARIANT
    }

    private fun getVariantRollenceValueForNewUpsellImprovement(): String {
        return RollenceKey.TREATMENT_VARIANT
    }
}
