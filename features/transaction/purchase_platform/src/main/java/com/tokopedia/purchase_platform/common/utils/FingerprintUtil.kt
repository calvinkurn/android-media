package com.tokopedia.purchase_platform.common.utils

import android.content.Context
import com.tokopedia.fingerprint.util.FingerprintConstant
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

/**
 * Created by Irfan Khoirul on 2019-08-14.
 */

class FingerprintUtil {

    companion object {

        @JvmStatic
        fun getEnableFingerprintPayment(context: Context): Boolean {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            return remoteConfig.getBoolean(FingerprintConstant.ENABLE_FINGERPRINT_MAINAPP)
        }

    }

}