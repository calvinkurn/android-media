package com.tokopedia.purchase_platform.common.utils

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import com.tokopedia.fingerprint.util.FingerprintConstant
import com.tokopedia.fingerprint.view.FingerPrintDialog
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import java.security.PublicKey

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

        @RequiresApi(Build.VERSION_CODES.M)
        @JvmStatic
        fun generatePublicKey(context: Context): PublicKey {
            return FingerPrintDialog.generatePublicKey(context);
        }

        @JvmStatic
        fun getPublicKeyString(publicKey: PublicKey): String {
            return FingerPrintDialog.getPublicKey(publicKey)
        }

    }

}