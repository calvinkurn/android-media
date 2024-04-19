package com.tokopedia.dev_monitoring_tools.config

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

class DevMonitoringToolsRemoteConfig {

    companion object {
        private const val ANDROID_MAIN_APP_DEV_MONITORING_TOOLS_CONFIG = "android_main_app_dev_monitoring_tools_config"
        private var devMonitoringToolsConfig: DevMonitoringToolsConfig? = null

        fun getConfig(context: Context): DevMonitoringToolsConfig {
            if (devMonitoringToolsConfig == null) {
                val remoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
                val dfRemoteConfig = remoteConfig.getString(ANDROID_MAIN_APP_DEV_MONITORING_TOOLS_CONFIG)
                dfRemoteConfig?.let {
                    try {
                        Gson().fromJson(dfRemoteConfig, DevMonitoringToolsConfig::class.java)?.let {
                            devMonitoringToolsConfig = it
                        }
                    } catch (e: Exception) { }
                }
            }
            return devMonitoringToolsConfig ?: DevMonitoringToolsConfig()
        }

        fun isEnableCopyCrashStackTraceToClipboardFeature(context: Context): Boolean {
            val remoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
            return remoteConfig.getBoolean(
                RemoteConfigKey.ANDROID_MAIN_APP_ENABLE_COPY_CRASH_STACK_TRACE_TO_CLIPBOARD,
                false
            )
        }
    }
}
