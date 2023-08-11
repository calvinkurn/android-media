package com.tokopedia.purchase_platform.common.revamp

import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import timber.log.Timber
import javax.inject.Inject

class CartCheckoutRevampRollenceManager @Inject constructor(private val abTestPlatform: AbTestPlatform) {

    private var currentValue: String? = null

    fun isRevamp(): Boolean {
        try {
            if (currentValue == null) {
                currentValue = abTestPlatform.getString(RollenceKey.CART_CHECKOUT_REVAMP)
            }
            return currentValue ?: "" == ""
        } catch (t: Throwable) {
            Timber.d(t)
            return getDefaultValue()
        }
    }

    private fun getDefaultValue(): Boolean {
        return false
    }
}
