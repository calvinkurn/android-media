package com.tokopedia.pageinfopusher

import android.app.Activity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import org.json.JSONObject
import timber.log.Timber

class PageInfoPusherManager(val activity: Activity) {

    val MAINAPP_GENERAL_INFO = "android_mainapp_general_info"
    val SELLERAPP_GENERAL_INFO = "android_sellerapp_general_info"

    val remoteConfig : RemoteConfig

    init {
        remoteConfig = FirebaseRemoteConfigImpl(activity)
    }

    fun showGeneralInfoMessage() {
        try {
            val rawConfig = if (GlobalConfig.isSellerApp()) {
                remoteConfig.getString(SELLERAPP_GENERAL_INFO)
            } else {
                remoteConfig.getString(MAINAPP_GENERAL_INFO)
            }

            if (rawConfig.isNullOrEmpty()) return

            val configList: org.json.JSONArray = org.json.JSONArray(rawConfig)
            for (i in 0 until configList.length()) {
                val config: JSONObject? = configList.optJSONObject(i)
                config?.let { handleConfig(config) }
            }
        } catch (e: java.lang.Exception) {
        }
    }

    private fun handleConfig(config: JSONObject) {
        try {
            val className: String? = activity.javaClass.canonicalName
            val pages: String? = config.optString("pages")
            val environment: String? = config.optString("environment")
            val appVersions: String? = config.optString("app_versions")
            val manufacturers: String? = config.optString("device_manufacturers")
            val models: String? = config.optString("device_models")
            val osVersions: String? = config.optString("android_os_versions")
            val message: String? = config.optString("message")

            if (!isEligibleForGeneralInfo(pages, className)) return
            if (!isEligibleForGeneralInfo(appVersions, GlobalConfig.VERSION_NAME)) return
            if (!isEligibleForGeneralInfo(manufacturers, android.os.Build.MANUFACTURER)) return
            if (!isEligibleForGeneralInfo(models, android.os.Build.MODEL)) return
            if (!isEligibleForGeneralInfo(osVersions, android.os.Build.VERSION.SDK_INT.toString())) return

            if ("all" != environment && GlobalConfig.isAllowDebuggingTools() && "dev" != environment) return
            if ("all" != environment && !GlobalConfig.isAllowDebuggingTools() && "prod" != environment) return

            Timber.w("P1#DISPLAY_GENERAL_INFO#'" + className
                    + "';dev='" + GlobalConfig.isAllowDebuggingTools()
                    + "';ver='" + GlobalConfig.VERSION_NAME
                    + "';message='" + message + "'")

            message?.let { showPopUp(it) }

        } catch (e: java.lang.Exception) {
        }
    }

    private fun isEligibleForGeneralInfo(requirements: String?, value: String?): Boolean {
        if (requirements.isNullOrEmpty()) return false

        val requirementList = requirements.split("\\s*,\\s*").map { s -> s.trim() }
        return ("all" == requirementList[0] || requirementList.contains(value))
    }

    private fun showPopUp(message: String) {
        android.app.AlertDialog.Builder(activity)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.action_dismiss, null)
                .show()
    }
}
