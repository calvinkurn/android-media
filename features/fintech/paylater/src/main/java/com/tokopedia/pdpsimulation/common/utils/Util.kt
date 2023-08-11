package com.tokopedia.pdpsimulation.common.utils

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

object Util {

    fun getTextRBPRemoteConfig(context: Context?, old: CharSequence?, new: CharSequence?): CharSequence? {
        return context?.let {
            if (isRBPOn(context)) new else old
        } ?: old
    }

    fun isRBPOn(context: Context?): Boolean {
        return context?.let {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            remoteConfig.getBoolean(
                RemoteConfigKey.ANDROID_FINTECH_ENABLE_RISK_BASED_PRICING,
                false
            )
        } ?: false
    }
}
