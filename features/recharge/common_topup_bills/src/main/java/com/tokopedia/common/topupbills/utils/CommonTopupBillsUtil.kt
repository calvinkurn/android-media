package com.tokopedia.common.topupbills.utils

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class CommonTopupBillsUtil {

    companion object {
        const val REMOTE_CONFIG_MAINAPP_DIGITAL_FAVORITE_NUMBER = "android_customer_enable_digital_favorite_number"

        fun isSeamlessFavoriteNumber(context: Context): Boolean {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            return remoteConfig.getBoolean(REMOTE_CONFIG_MAINAPP_DIGITAL_FAVORITE_NUMBER, true)
        }

        fun getApplinkFavoriteNumber(context: Context): String {
            return if (isSeamlessFavoriteNumber(context))
                ApplinkConsInternalDigital.FAVORITE_NUMBER else ApplinkConsInternalDigital.SEARCH_NUMBER
        }
    }
}