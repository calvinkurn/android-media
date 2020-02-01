package com.tokopedia.dynamicfeatures.config

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class DFRemoteConfig {

    companion object {
        private const val ANDROID_DF_CONFIG = "android_df_config"
    }

    public fun getDfConfig(context: Context): DFConfig {
        val remoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
        val dfRemoteConfig = remoteConfig.getString(ANDROID_DF_CONFIG)
        dfRemoteConfig?.let {
            return Gson().fromJson(dfRemoteConfig, DFConfig::class.java)
        }
        return DFConfig()
    }
}