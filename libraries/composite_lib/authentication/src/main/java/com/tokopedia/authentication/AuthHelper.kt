package com.tokopedia.authentication

import android.content.Context
import android.os.Build
import android.support.v4.util.ArrayMap
import android.util.Base64
import com.google.gson.Gson
import com.tokopedia.network.NetworkRouter
import com.tokopedia.config.GlobalConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class AuthHelper {
    companion object {
        @JvmStatic
        fun getDefaultHeaderMap(
                path: String,
                strParam: String,
                method: String,
                contentType: String,
                authKey: String,
                dateFormat: String,
                userId: String,
                session: UserSessionInterface
        ): MutableMap<String, String> {
            val date = generateDate(dateFormat)
            val contentMD5 = generateContentMd5(strParam)
            val authString = "${method}\n${contentMD5}\n${contentType}\n${date}\n${path}"
            val signature = calculateRFC2104HMAC(authString, authKey)

            val headerMap = ArrayMap<String, String>()
            headerMap[AuthConstant.HEADER_CONTENT_TYPE] = contentType
            headerMap[AuthConstant.HEADER_X_METHOD] = method
            headerMap[AuthConstant.HEADER_REQUEST_METHOD] = method
            headerMap[AuthConstant.HEADER_CONTENT_MD5] = contentMD5
            headerMap[AuthConstant.HEADER_DATE] = date
            headerMap[AuthConstant.HEADER_AUTHORIZATION] = "${AuthConstant.HEADER_HMAC_SIGNATURE_KEY}${signature.trim()}"

            headerMap.remove(AuthConstant.HEADER_ACCOUNT_AUTHORIZATION)

            headerMap[AuthConstant.HEADER_ACCOUNT_AUTHORIZATION] = "${AuthConstant.HEADER_PARAM_BEARER} ${session.accessToken}"
            headerMap[AuthConstant.HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString(10)
            headerMap[AuthConstant.HEADER_X_TKPD_APP_NAME] = GlobalConfig.getPackageApplicationName()
            headerMap[AuthConstant.HEADER_X_TKPD_APP_VERSION] = "android-${GlobalConfig.VERSION_NAME}"
            headerMap[AuthConstant.HEADER_OS_VERSION] = Build.VERSION.SDK_INT.toString(10)
            headerMap[AuthConstant.HEADER_USER_AGENT] = getUserAgent()
            headerMap[AuthConstant.HEADER_USER_ID] = userId
            headerMap[AuthConstant.HEADER_DEVICE] = "android-${GlobalConfig.VERSION_NAME}"

            return headerMap
        }

        @JvmStatic
        fun getDefaultHeaderMapOld(
                path: String,
                strParam: String,
                method: String,
                contentType: String,
                authKey: String,
                dateFormat: String,
                userId: String,
                session: UserSessionInterface
        ): MutableMap<String, String> {
            val date = generateDate(dateFormat)
            val contentMD5 = generateContentMd5(strParam)
            val authString = "${method}\n${contentMD5}\n${contentType}\n${date}\n${path}"
            val signature = calculateRFC2104HMAC(authString, authKey)

            val headerMap = ArrayMap<String, String>()
            headerMap[AuthConstant.HEADER_CONTENT_TYPE] = contentType
            headerMap[AuthConstant.HEADER_X_METHOD] = method
            headerMap[AuthConstant.HEADER_REQUEST_METHOD] = method
            headerMap[AuthConstant.HEADER_CONTENT_MD5] = contentMD5
            headerMap[AuthConstant.HEADER_DATE] = date
            headerMap[AuthConstant.HEADER_AUTHORIZATION] = "TKPD Tokopedia:${signature.trim()}"

            headerMap.remove(AuthConstant.HEADER_ACCOUNT_AUTHORIZATION)

            headerMap[AuthConstant.HEADER_ACCOUNT_AUTHORIZATION] = "${AuthConstant.HEADER_PARAM_BEARER} ${session.accessToken}"
            headerMap[AuthConstant.HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString(10)
            headerMap[AuthConstant.HEADER_X_TKPD_APP_NAME] = GlobalConfig.getPackageApplicationName()
            headerMap[AuthConstant.HEADER_X_TKPD_APP_VERSION] = "android-${GlobalConfig.VERSION_NAME}"
            headerMap[AuthConstant.HEADER_OS_VERSION] = Build.VERSION.SDK_INT.toString(10)
            headerMap[AuthConstant.HEADER_USER_AGENT] = getUserAgent()

            headerMap[AuthConstant.HEADER_USER_ID] = userId
            headerMap[AuthConstant.HEADER_DEVICE] = "android-${GlobalConfig.VERSION_NAME}"

            return headerMap
        }

        @JvmStatic
        fun generateHeaders(
                path: String,
                strParam: String,
                method: String,
                authKey: String,
                contentType: String?,
                userId: String,
                userSessionInterface: UserSessionInterface
        ): MutableMap<String, String> {
            val finalHeader = getDefaultHeaderMap(
                    path,
                    strParam,
                    method,
                    contentType ?: AuthConstant.CONTENT_TYPE,
                    authKey,
                    AuthConstant.DATE_FORMAT, userId, userSessionInterface
            )
            finalHeader[AuthConstant.HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString(10)

            return finalHeader
        }

        @JvmStatic
        fun generateHeadersAccount(authKey: String): MutableMap<String, String> {
            val clientID = "7ea919182ff"
            val clientSecret = "b36cbf904d14bbf90e7f25431595a364"
            val encodeString = "${clientID}:${clientSecret}"

            val finalHeader = HashMap<String, String>()
            finalHeader[AuthConstant.HEADER_CONTENT_TYPE] = AuthConstant.CONTENT_TYPE
            finalHeader[AuthConstant.HEADER_CACHE_CONTROL] = "no-cache"

            if (authKey.isEmpty()) {
                finalHeader[AuthConstant.HEADER_AUTHORIZATION] = "Basic ${AuthHelperJava.base64Encoder(encodeString, Base64.NO_WRAP)}"
            } else {
                finalHeader[AuthConstant.HEADER_AUTHORIZATION] = authKey
            }

            finalHeader[AuthConstant.HEADER_DEVICE] = "android-${GlobalConfig.VERSION_NAME}"
            finalHeader[AuthConstant.HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString(10)
            finalHeader[AuthConstant.HEADER_X_TKPD_APP_NAME] = GlobalConfig.getPackageApplicationName()
            finalHeader[AuthConstant.HEADER_X_TKPD_APP_VERSION] = "android-${GlobalConfig.VERSION_NAME}"

            return finalHeader
        }

        @JvmStatic
        fun generateHeadersWithPath(
                path: String,
                strParam: String,
                method: String,
                authKey: String,
                contentType: String?,
                userId: String,
                userSessionInterface: UserSessionInterface
        ): MutableMap<String, String> {
            val finalHeader = getDefaultHeaderMapOld(
                    path,
                    strParam,
                    method,
                    contentType ?: AuthConstant.CONTENT_TYPE,
                    authKey,
                    AuthConstant.DATE_FORMAT,
                    userId,
                    userSessionInterface
            ) as ArrayMap<String, String>

            finalHeader[AuthConstant.HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString(10)
            finalHeader[AuthConstant.HEADER_PATH] = path

            return finalHeader
        }

        @JvmStatic
        fun generateParamsNetwork(
                userId: String,
                deviceId: String,
                params: MutableMap<String, String>
        ): MutableMap<String, String> {
            val hash = AuthHelperJava.md5("${userId}~${deviceId}")

            params[AuthConstant.PARAM_USER_ID] = userId
            params[AuthConstant.PARAM_DEVICE_ID] = deviceId
            params[AuthConstant.PARAM_HASH] = hash
            params[AuthConstant.PARAM_OS_TYPE] = "1"
            params[AuthConstant.PARAM_TIMESTAMP] = (Date().time / 1000).toString(10)

            return params
        }

        @JvmStatic
        fun getHeaderRequestReactNative(context: Context): String {
            val session = UserSession(context)

            val header = HashMap<String, String>()
            header[AuthConstant.HEADER_SESSION_ID] = session.deviceId
            header[AuthConstant.HEADER_TKPD_USER_ID] = if (session.isLoggedIn) session.userId else "0"
            header[AuthConstant.HEADER_AUTHORIZATION] = "Bearer ${session.accessToken}"

            header.remove(AuthConstant.HEADER_ACCOUNT_AUTHORIZATION)

            header[AuthConstant.HEADER_ACCOUNT_AUTHORIZATION] = "${AuthConstant.HEADER_PARAM_BEARER} ${session.accessToken}"
            header[AuthConstant.PARAM_OS_TYPE] = "1"
            header[AuthConstant.HEADER_DEVICE] = "android-${GlobalConfig.VERSION_NAME}"
            header[AuthConstant.HEADER_USER_ID] = if (session.isLoggedIn) session.userId else "0"
            header[AuthConstant.HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString()
            header[AuthConstant.HEADER_X_TKPD_USER_ID] = if (session.isLoggedIn) session.userId else "0"
            header[AuthConstant.HEADER_X_TKPD_APP_NAME] = GlobalConfig.getPackageApplicationName()
            header[AuthConstant.HEADER_X_TKPD_APP_VERSION] = "android-${GlobalConfig.VERSION_NAME}"

            return Gson().toJson(header)
        }

        @JvmStatic
        fun getAuthHeaderReact(
                context: Context,
                path: String,
                strParam: String,
                method: String,
                contentType: String
        ): MutableMap<String, String> {
            val session = UserSession(context)
            val headers = getDefaultHeaderMap(
                    path,
                    strParam,
                    method,
                    contentType,
                    AuthKey.KEY_WSV4_NEW,
                    AuthConstant.DATE_FORMAT,
                    session.userId,
                    session
            ) as ArrayMap<String, String>

            headers[AuthConstant.HEADER_SESSION_ID] = session.deviceId
            headers[AuthConstant.HEADER_TKPD_USER_ID] = if (session.isLoggedIn) session.userId else "0"

            headers.remove(AuthConstant.HEADER_ACCOUNT_AUTHORIZATION)

            headers[AuthConstant.HEADER_ACCOUNT_AUTHORIZATION] = "${AuthConstant.HEADER_PARAM_BEARER} ${session.accessToken}"
            headers[AuthConstant.PARAM_OS_TYPE] = "1"
            headers[AuthConstant.HEADER_DEVICE] = "android-${GlobalConfig.VERSION_NAME}"
            headers[AuthConstant.HEADER_X_TKPD_USER_ID] = if (session.isLoggedIn) session.userId else "0"

            if (context.applicationContext is NetworkRouter) {
                val fingerprintModel = (context.applicationContext as NetworkRouter).getFingerprintModel()
                val json = fingerprintModel.getFingerprintHash()

                headers[AuthConstant.KEY_FINGERPRINT_HASH] = AuthHelperJava.md5("${json}+${session.userId}")
                AuthHelperJava.md5(json + "+" + session.userId)
                headers[AuthConstant.KEY_FINGERPRINT_DATA] = json
            }

            return headers
        }

        @JvmStatic
        fun calculateRFC2104HMAC(authString: String, authKey: String): String {
            try {
                val signingKey = SecretKeySpec(authKey.toByteArray(), AuthConstant.MAC_ALGORITHM)
                val mac = Mac.getInstance(AuthConstant.MAC_ALGORITHM)
                mac.init(signingKey)
                val rawHmac = mac.doFinal(authString.toByteArray())

                return AuthHelperJava.base64Encoder(rawHmac, Base64.DEFAULT)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()

                return ""
            } catch (e: InvalidKeyException) {
                e.printStackTrace()

                return ""
            }
        }

        @JvmStatic
        fun generateContentMd5(s: String): String {
            return AuthHelperJava.md5(s)
        }

        @JvmStatic
        fun generateDate(dateFormat: String): String {
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)

            return simpleDateFormat.format(Date())
        }

        @JvmStatic
        fun getUserAgent(): String {
            return "TkpdConsumer/${GlobalConfig.VERSION_NAME} (Android ${Build.VERSION.RELEASE};)"
        }
    }
}
