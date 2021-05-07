package com.tokopedia.applink.teleporter

import android.content.Context
import android.net.Uri
import android.text.TextUtils
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
    const val DURATION_FETCH = 1800000
    const val MAINAPP_SWITCH_TO_WEBVIEW = "android_mainapp_switch_to_webview"
    const val SELLERAPP_SWITCH_TO_WEBVIEW = "android_sellerapp_switch_to_webview"

    fun switchToWebviewIfNeeded(context: Context, mappedLink: String, originalLink: String): String {
        try {
            val now = System.currentTimeMillis()
            if (now - lastFetch > DURATION_FETCH) {
                getLatestConfig(context)
                lastFetch = now
            }
            val jsonObj = configJSON ?: return mappedLink

            val link = if (mappedLink.isNotEmpty()) mappedLink else originalLink
            val uri = Uri.parse(link)
            val trimmedDeeplink = UriUtil.trimDeeplink(uri, link)

            val switchData = jsonObj.optJSONObject(trimmedDeeplink) ?: return mappedLink

            val environment = switchData.optString("environment")
            val versions = switchData.optString("versions")
            val weblink = switchData.optString("weblink")

            if (GlobalConfig.isAllowDebuggingTools() && environment != "dev") return mappedLink
            if (!GlobalConfig.isAllowDebuggingTools() && environment != "prod") return mappedLink

            val versionList = versions.split(",")
            if (GlobalConfig.VERSION_NAME !in versionList) return mappedLink

            val webviewApplink = UriUtil.buildUri(ApplinkConstInternalGlobal.WEBVIEW, weblink)

            ServerLogger.log(Priority.P1, "WEBVIEW_SWITCH", mapOf("type" to link, "url" to weblink))

            return DeeplinkMapper.createAppendDeeplinkWithQuery(webviewApplink, uri.query)
        } catch (e: Exception) {
            return mappedLink
        }
    }

    private fun getLatestConfig(context: Context) {
        try {
            val remoteConfig = FirebaseRemoteConfigInstance.get(context)
            var webviewSwitchConfig = ""

            if (GlobalConfig.isSellerApp()) {
                webviewSwitchConfig = remoteConfig.getString(SELLERAPP_SWITCH_TO_WEBVIEW)
            } else {
                webviewSwitchConfig = remoteConfig.getString(MAINAPP_SWITCH_TO_WEBVIEW)
            }

            if (webviewSwitchConfig != null) {
                configJSON = JSONObject(webviewSwitchConfig)
            }
        } catch (e: Exception) {
            configJSON = null
        }

    }
}