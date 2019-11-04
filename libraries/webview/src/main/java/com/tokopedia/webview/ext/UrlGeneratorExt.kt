package com.tokopedia.webview.ext

import android.content.Context
import android.net.Uri
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp

/**
 * Append "appClientId" as query parameter in the inputted url
 * example:
 * http://www.tokopedia.com/pulsa -> http://www.tokopedia.com/pulsa?appClientId=123
 * This is used for tracking in the url
 */
fun appendGAClientIdAsQueryParam(url: String?, context: Context?): String? {
    if (url.isNullOrEmpty()) {
        return ""
    }
    if (isPassingGAClientIdEnable(context)) {
        try {
            val decodedUrl = url.decode()
            val uri = Uri.parse(decodedUrl)
            val clientID = TrackApp.getInstance().gtm.clientIDString
            if (clientID != null) {
                return uri.buildUpon().appendQueryParameter("appClientId", clientID)
                    .build().toString()
                    .encodeOnce()
            }
        } catch (ignored: Exception) {
        }
    }
    return url.encodeOnce()
}

private fun isPassingGAClientIdEnable(context: Context?): Boolean {
    if (context == null) return false

    val remoteConfig = FirebaseRemoteConfigImpl(context)
    return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PASS_GA_CLIENT_ID_WEB, true)
}
