package com.tokopedia.purchase_platform.common.revamp

import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import javax.inject.Inject

class CartCheckoutRevampRollenceManager @Inject constructor(private val abTestPlatform: AbTestPlatform) {

    fun getValue(): String {
        return abTestPlatform.getString(RollenceKey.CART_CHECKOUT_REVAMP)
    }
}
