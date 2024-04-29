package com.tokopedia.purchase_platform.common.revamp

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

class CartCheckoutRevampRollenceManager @Inject constructor() {

    fun isRevamp(): Boolean {
        return true
    }

    fun shouldRedirectToNewCheckoutPayment(context: Context): Boolean {
        return FirebaseRemoteConfigImpl(context.applicationContext).getBoolean(RemoteConfigKey.ANDROID_ENABLE_CHECKOUT_PAYMENT, getDefaultValue())
    }

    private fun getDefaultValue(): Boolean {
        return true
    }
}
