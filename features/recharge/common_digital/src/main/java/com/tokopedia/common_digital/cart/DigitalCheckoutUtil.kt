package com.tokopedia.common_digital.cart

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

class DigitalCheckoutUtil {

    companion object {

        fun getApplinkCartDigital(context: Context): String {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            val getDigitalCart = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_RECHARGE_CHECKOUT, true)
            return if (getDigitalCart)
                ApplinkConsInternalDigital.CHECKOUT_DIGITAL else ApplinkConsInternalDigital.CART_DIGITAL
        }
    }
}