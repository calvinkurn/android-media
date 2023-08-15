package com.tokopedia.purchase_platform.common.revamp

import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import javax.inject.Inject

class CartCheckoutRevampRollenceManager @Inject constructor(private val abTestPlatform: AbTestPlatform) {

    private var currentValue: String? = null

    fun isRevamp(): Boolean {
//        try {
//            if (currentValue == null) {
//                currentValue = abTestPlatform.getString(RollenceKey.CART_CHECKOUT_REVAMP)
//            }
//            return (currentValue ?: "") == RollenceKey.CART_CHECKOUT_NEW
//        } catch (t: Throwable) {
//            Timber.d(t)
//            return getDefaultValue()
//        }
        return true
    }

    private fun getDefaultValue(): Boolean {
        return false
    }
}
