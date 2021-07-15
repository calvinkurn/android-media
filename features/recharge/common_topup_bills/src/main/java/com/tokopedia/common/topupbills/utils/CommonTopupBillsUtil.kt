package com.tokopedia.common.topupbills.utils

import android.content.Context
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class CommonTopupBillsUtil {

    companion object {

        fun isSeamlessFavoriteNumber(context: Context): Boolean {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            return remoteConfig.getBoolean(DeeplinkMapperDigital.REMOTE_CONFIG_MAINAPP_DIGITAL_FAVORITE_NUMBER, true)
        }

        fun getApplinkFavoriteNumber(context: Context): String {
            return if (isSeamlessFavoriteNumber(context))
                ApplinkConsInternalDigital.FAVORITE_NUMBER else ApplinkConsInternalDigital.SEARCH_NUMBER
        }
    }
}