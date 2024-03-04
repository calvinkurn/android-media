package com.tokopedia.sessioncommon.network

import android.content.Context
import android.os.Build
import androidx.collection.ArrayMap
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.authentication.AuthHelper.Companion.calculateRFC2104HMAC
import com.tokopedia.network.authentication.AuthHelper.Companion.generateDate
import com.tokopedia.network.authentication.AuthHelper.Companion.getMD5Hash
import com.tokopedia.network.authentication.AuthKey
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Named

/**
 * @author by nisie on 10/16/18.
 */
open class TkpdOldAuthInterceptor : TkpdAuthInterceptor {
    constructor(context: Context?, networkRouter: NetworkRouter?, userSession: UserSession?) : super(context, networkRouter, userSession)
    constructor(context: Context?, networkRouter: NetworkRouter?,
                @Named(SessionModule.SESSION_MODULE) userSession: UserSessionInterface?) : super(context, networkRouter, userSession)

    constructor(context: Context?, networkRouter: NetworkRouter?, userSession: UserSession?, authKey: String?) : super(context, networkRouter, userSession, authKey)

    override fun getHeaderMap(path: String, strParam: String, method: String, authKey: String, contentTypeHeader: String): Map<String, String> {
        val finalHeader = getDefaultHeaderMap(
                path, strParam, method, contentTypeHeader
                ?: CONTENT_TYPE,
                authKey, DATE_FORMAT, userSession.userId, headerTheme
        )
        finalHeader[HEADER_X_APP_VERSION] = Integer.toString(GlobalConfig.VERSION_CODE)
        return finalHeader
    }

    companion object {
        private const val CONTENT_TYPE = "application/x-www-form-urlencoded"
        private const val DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ"
        private const val HEADER_X_APP_VERSION = "X-APP-VERSION"
        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val HEADER_X_METHOD = "X-Method"
        private const val HEADER_REQUEST_METHOD = "Request-Method"
        private const val HEADER_CONTENT_MD5 = "Content-MD5"
        private const val HEADER_DATE = "Date"
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val HEADER_USER_ID = "X-User-ID"
        private const val HEADER_DEVICE = "X-Device"
        private const val HEADER_X_TKPD_APP_NAME = "X-Tkpd-App-Name"
        private const val HEADER_X_TKPD_APP_VERSION = "X-Tkpd-App-Version"
        private const val HEADER_OS_VERSION = "os_version"
        private const val HEADER_HMAC_SIGNATURE_KEY = "TKPD Tokopedia:"
        private const val PARAM_X_TKPD_USER_ID = "x-tkpd-userid"
        private const val HEADER_PATH = "x-tkpd-path"
        private const val HEADER_RELEASE_TRACK = "x-release-track"
        private const val HEADER_X_THEME = "x-theme"
        fun getDefaultHeaderMap(path: String, strParam: String?, method: String,
                                contentType: String, authKey: String?,
                                dateFormat: String?, userId: String,
                                theme: String): MutableMap<String, String> {
            val date = generateDate(dateFormat!!)
            val contentMD5 = getMD5Hash(strParam!!)
            val authString = """
                 $method
                 $contentMD5
                 $contentType
                 $date
                 $path
                 """.trimIndent()
            val signature = calculateRFC2104HMAC(authString, AuthKey.KEY_WSV4)
            val headerMap: MutableMap<String, String> = ArrayMap()
            headerMap[HEADER_CONTENT_TYPE] = contentType
            headerMap[HEADER_X_METHOD] = method
            headerMap[HEADER_REQUEST_METHOD] = method
            headerMap[HEADER_CONTENT_MD5] = contentMD5
            headerMap[HEADER_DATE] = date
            headerMap[HEADER_AUTHORIZATION] = HEADER_HMAC_SIGNATURE_KEY + signature.trim { it <= ' ' }
            headerMap[HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString()
            headerMap[HEADER_X_TKPD_APP_NAME] = GlobalConfig.getPackageApplicationName()
            headerMap[HEADER_X_TKPD_APP_VERSION] = "android-" + GlobalConfig.VERSION_NAME
            headerMap[HEADER_RELEASE_TRACK] = GlobalConfig.VERSION_NAME_SUFFIX
            headerMap[HEADER_OS_VERSION] = Build.VERSION.SDK_INT.toString()
            headerMap[HEADER_USER_ID] = userId
            headerMap[HEADER_DEVICE] = "android-" + GlobalConfig.VERSION_NAME
            headerMap[HEADER_PATH] = path
            headerMap[HEADER_X_THEME] = theme
            return headerMap
        }
    }
}
