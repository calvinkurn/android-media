package com.tokopedia.checkout.utils

import android.content.Context
import android.os.Build
import com.tokopedia.fingerprint.FingerprintUtil
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import java.security.PublicKey

object CheckoutFingerprintUtil {

    fun getEnableFingerprintPayment(context: Context?): Boolean {
        return if (context != null) {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            remoteConfig.getBoolean(FingerprintUtil.ENABLE_FINGERPRINT_MAINAPP)
        } else {
            false
        }
    }

    fun getFingerprintPublicKey(context: Context?): PublicKey? {
        return if (context != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FingerprintUtil.generatePublicKey(context)
            } else {
                null
            }
        } else {
            null
        }
    }
}
