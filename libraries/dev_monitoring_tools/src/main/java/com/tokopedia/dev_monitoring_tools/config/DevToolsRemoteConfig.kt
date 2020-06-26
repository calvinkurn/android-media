package com.tokopedia.dev_monitoring_tools.config

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class DevToolsRemoteConfig {

    companion object {
        private const val ANDROID_MAIN_APP_DEV_MONITORING_TOOLS_CONFIG = "android_main_app_dev_monitoring_tools_config"
        private var devToolsConfig: DevToolsConfig? = null

        fun getConfig(context: Context): DevToolsConfig {
            if (devToolsConfig == null) {
                val remoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
                val dfRemoteConfig = remoteConfig.getString(ANDROID_MAIN_APP_DEV_MONITORING_TOOLS_CONFIG)
                dfRemoteConfig?.let {
                    Gson().fromJson(dfRemoteConfig, DevToolsConfig::class.java)?.let {
                        devToolsConfig = it
                    }
                }
            }
            return devToolsConfig ?: DevToolsConfig()
        }
    }
}