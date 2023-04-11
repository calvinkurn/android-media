package com.tokopedia.checkout.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import timber.log.Timber

object ShipmentRollenceUtil {

    @JvmStatic
    fun enableCheckoutNewUpsellImprovement(): Boolean {
        val remoteConfigRollenceValue = try {
            RemoteConfigInstance.getInstance()
                .abTestPlatform.getString(
                    RollenceKey.CHECKOUT_PLUS_NEW_UPSELL_IMPROVEMENT,
                    getDefaultRollenceValueForNewUpsellImprovement()
                )
        } catch (e: Exception) {
            Timber.e(e)
            getDefaultRollenceValueForNewUpsellImprovement()
        }
        return remoteConfigRollenceValue == getVariantRollenceValueForNewUpsellImprovement()
    }

    private fun getDefaultRollenceValueForNewUpsellImprovement(): String {
        return RollenceKey.CONTROL_VARIANT
    }

    private fun getVariantRollenceValueForNewUpsellImprovement(): String {
        return RollenceKey.TREATMENT_VARIANT
    }
}
