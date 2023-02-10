package com.tokopedia.dynamicfeatures.config

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class DFRemoteConfig {

    companion object {
        private const val ANDROID_MAIN_APP_DF_CONFIG = "android_main_app_df_config"
        private var dfConfig: DFConfig? = null

        fun getConfig(context: Context): DFConfig {
            if (dfConfig == null) {
                val remoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
                val dfRemoteConfig = remoteConfig.getString(ANDROID_MAIN_APP_DF_CONFIG)
                dfRemoteConfig?.let {
                    Gson().fromJson(dfRemoteConfig, DFConfig::class.java)?.let {
                        dfConfig = it
                    }
                }
            }
            return dfConfig ?: DFConfig()
        }
    }
}
