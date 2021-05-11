package com.tokopedia.applink.teleporter

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import org.json.JSONObject

object Teleporter {
    var configJSON: JSONObject? = null
    var lastFetch: Long = 0L
    const val DURATION_FETCH = 900000
    const val MAINAPP_SWITCH_TO_WEBVIEW = "android_mainapp_teleporter"
    const val SELLERAPP_SWITCH_TO_WEBVIEW = "android_sellerapp_teleporter"

    fun switchIfNeeded(context: Context, uri: Uri): String {
        try {
            val now = System.currentTimeMillis()
            if (now - lastFetch > DURATION_FETCH) {
                getLatestConfig(context)
                lastFetch = now
            }
            val jsonObj = configJSON ?: return ""

            val trimmedDeeplink = UriUtil.trimDeeplink(uri, uri.toString())

            val switchData = jsonObj.optJSONObject(trimmedDeeplink) ?: return ""

            val environment = switchData.optString("env")
            val versions = switchData.optString("versions")
            val weblink = switchData.optString("link")

            if (GlobalConfig.isAllowDebuggingTools() && environment != "dev") return ""
            if (!GlobalConfig.isAllowDebuggingTools() && environment != "prod") return ""

            val versionList = versions.split(",")
            if (GlobalConfig.VERSION_NAME !in versionList) return ""

            val webviewApplink = UriUtil.buildUri(ApplinkConstInternalGlobal.WEBVIEW, weblink)

            ServerLogger.log(Priority.P1, "WEBVIEW_SWITCH", mapOf("type" to uri.toString(), "url" to weblink))

            return DeeplinkMapper.createAppendDeeplinkWithQuery(webviewApplink, uri.query)
        } catch (e: Exception) {
            return ""
        }
    }

    private fun getLatestConfig(context: Context) {
        try {
            val remoteConfig = FirebaseRemoteConfigInstance.get(context)
            var webviewSwitchConfig = ""

            webviewSwitchConfig = if (GlobalConfig.isSellerApp()) {
                remoteConfig.getString(SELLERAPP_SWITCH_TO_WEBVIEW)
            } else {
                remoteConfig.getString(MAINAPP_SWITCH_TO_WEBVIEW)
            }

            if (webviewSwitchConfig != null) {
                configJSON = JSONObject(webviewSwitchConfig)
            }
        } catch (e: Exception) {
            configJSON = null
        }

    }
}