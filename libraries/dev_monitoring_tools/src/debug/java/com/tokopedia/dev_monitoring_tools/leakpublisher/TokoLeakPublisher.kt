package com.tokopedia.dev_monitoring_tools.leakpublisher

import android.app.Application
import com.gojek.leak.publisher.LeakPublisher
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig

class TokoLeakPublisher(
    application: Application,
    gson: Gson,
    isEnable: Boolean,
    isEnableStrictMode: Boolean
) {

    init {
        val buildTypeApp = if (GlobalConfig.isAllowDebuggingTools()) "debug" else "release"
        val metadata = mapOf<String, Any>(
            "Version Name" to GlobalConfig.VERSION_NAME,
            "Version Code" to GlobalConfig.VERSION_CODE,
            "App Id" to GlobalConfig.APPLICATION_ID,
            "Build Type" to buildTypeApp
        )

        LeakPublisher(
            app = application,
            gson = gson,
            metadata = metadata,
            isStrictModeEnabled = isEnableStrictMode,
            isLeakCanaryEnabled = isEnable,
            isObfuscated = false,
            targetPackage = "tkpd",
            registrar = TokoLeakNetworkRegistrar.getInstance()
        ).execute()
    }

    companion object {
        @Volatile
        private var INSTANCE: TokoLeakPublisher? = null

        fun getInstance(
            application: Application,
            gson: Gson,
            isEnable: Boolean,
            isEnableStrictMode: Boolean
        ): TokoLeakPublisher {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TokoLeakPublisher(application, gson, isEnable, isEnableStrictMode).also { INSTANCE = it }
            }
        }
    }
}
