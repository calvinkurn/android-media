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
                    getDefaultRollenceValue()
                )
            return (remoteConfigRollenceValue == getVariantRollenceValue())
        } catch (e: Exception) {
            Timber.e(e)
            return false
        }
    }

    private fun getDefaultRollenceValue(): String {
        return RollenceKey.CONTROL_VARIANT
    }

    private fun getVariantRollenceValue(): String {
        return RollenceKey.EXPERIMENT_VARIANT
    }
}
