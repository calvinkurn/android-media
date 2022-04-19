package com.tokopedia.favorite.data.source.apis.interceptor

import android.content.Context
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.network.authentication.HEADER_AUTHORIZATION
import com.tokopedia.network.authentication.HEADER_RELEASE_TRACK
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import java.text.SimpleDateFormat
import java.util.*

class TopAdsAuthInterceptor(
        context: Context?,
        networkRouter: NetworkRouter?,
        userSessionInterface: UserSessionInterface?
) : TkpdAuthInterceptor(context, networkRouter, userSessionInterface) {

    companion object {
        var VERSION_NAME = "1.0"
    }

    override fun getHeaderMap(
            path: String,
            strParam: String,
            method: String,
            authKey: String,
            contentTypeHeader: String
    ): Map<String, String> {
        var contentType = ""
        when (method) {
            "PATCH", "POST" -> contentType = "application/json"
            "GET" -> {
            }
            else -> {
            }
        }
        val headerMap = AuthHelper.getDefaultHeaderMap(path,
                strParam,
                method,
                contentType,
                authKey,
                "dd MMM yy HH:mm ZZZ",
                userSession)

        val dateFormat = SimpleDateFormat("dd MMM yy HH:mm ZZZ", Locale.ENGLISH)
        val date = dateFormat.format(Date())
        val headerAuth = headerMap[HEADER_AUTHORIZATION]
        headerMap["X-Date"] = date
        headerMap["X-Device"] = "android-$VERSION_NAME"
        headerMap["X-Tkpd-Authorization"] = headerAuth ?: ""
        headerMap["Authorization"] = "Bearer " + userSession.accessToken
        headerMap[HEADER_RELEASE_TRACK] = GlobalConfig.VERSION_NAME_SUFFIX
        return headerMap
    }

}
