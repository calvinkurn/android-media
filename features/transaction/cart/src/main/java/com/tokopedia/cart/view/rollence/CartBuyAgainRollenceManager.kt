package com.tokopedia.cart.view.rollence

import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import timber.log.Timber
import javax.inject.Inject

class CartBuyAgainRollenceManager @Inject constructor(
    private val abTestPlatform: AbTestPlatform
) {

    private var currentValue: String? = null

    fun isBuyAgainCartEnabled(): Boolean {
        return try {
            if (currentValue == null) {
                currentValue = abTestPlatform.getString(RollenceKey.CART_BUY_AGAIN, RollenceKey.CART_BUY_AGAIN_CONTROL)
            }
            (currentValue ?: "") == RollenceKey.CART_BUY_AGAIN_VARIANT
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            getDefaultValue()
        }
    }

    private fun getDefaultValue(): Boolean {
        return false
    }
}
