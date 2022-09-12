package com.tokopedia.network.authentication

import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.collection.ArrayMap
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.UserSessionInterface
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class AuthHelper {
    companion object {

        @JvmStatic
        fun getVersionName(versionName: String): Pair<String, String> {
            val versionNameStripIndex = versionName.indexOf("-")
            return if (versionNameStripIndex > 0) {
                Pair(
                    versionName.substring(0, versionNameStripIndex),
                    versionName.substring(versionNameStripIndex) + 1
                )
            } else {
                Pair(versionName, "")
            }
        }

        @JvmStatic
        fun getDefaultHeaderMap(
            path: String,
            strParam: String,
            method: String,
            contentType: String,
            authKey: String,
            dateFormat: String,
            userSession: UserSessionInterface
        ): MutableMap<String, String> {
            val date = generateDate(dateFormat)
            val contentMD5 = getMD5Hash(strParam)
            val authString = "${method}\n${contentMD5}\n${contentType}\n${date}\n${path}"
            val signature = calculateRFC2104HMAC(authString, authKey)

            val headerMap = ArrayMap<String, String>()
            headerMap[HEADER_CONTENT_TYPE] = contentType
            headerMap[HEADER_X_METHOD] = method
            headerMap[HEADER_REQUEST_METHOD] = method
            headerMap[HEADER_CONTENT_MD5] = contentMD5
            headerMap[HEADER_DATE] = date
            headerMap[HEADER_AUTHORIZATION] = "${HEADER_HMAC_SIGNATURE_KEY}${signature.trim()}"

            headerMap.remove(HEADER_ACCOUNT_AUTHORIZATION)
            headerMap.remove(HEADER_RELEASE_TRACK)

            headerMap[HEADER_RELEASE_TRACK] = GlobalConfig.VERSION_NAME_SUFFIX
            headerMap[HEADER_ACCOUNT_AUTHORIZATION] =
                "$HEADER_PARAM_BEARER ${userSession.accessToken}"
            headerMap[HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString(10)
            headerMap[HEADER_X_TKPD_APP_NAME] = GlobalConfig.getPackageApplicationName()
            headerMap[HEADER_X_TKPD_APP_VERSION] = "android-${GlobalConfig.VERSION_NAME}"
            headerMap[HEADER_OS_VERSION] = Build.VERSION.SDK_INT.toString(10)
            headerMap[HEADER_USER_AGENT] = getUserAgent()
            headerMap[HEADER_USER_ID] = userSession.userId
            headerMap[HEADER_DEVICE] = "android-${GlobalConfig.VERSION_NAME}"

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
            val contentMD5 = getMD5Hash(strParam)
            val authString = "${method}\n${contentMD5}\n${contentType}\n${date}\n${path}"
            val signature = calculateRFC2104HMAC(authString, authKey)

            val headerMap = ArrayMap<String, String>()
            headerMap[HEADER_CONTENT_TYPE] = contentType
            headerMap[HEADER_X_METHOD] = method
            headerMap[HEADER_REQUEST_METHOD] = method
            headerMap[HEADER_CONTENT_MD5] = contentMD5
            headerMap[HEADER_DATE] = date
            headerMap[HEADER_AUTHORIZATION] = "TKPD Tokopedia:${signature.trim()}"

            headerMap.remove(HEADER_ACCOUNT_AUTHORIZATION)
            headerMap.remove(HEADER_RELEASE_TRACK)

            headerMap[HEADER_RELEASE_TRACK] = GlobalConfig.VERSION_NAME_SUFFIX
            headerMap[HEADER_ACCOUNT_AUTHORIZATION] = "$HEADER_PARAM_BEARER ${session.accessToken}"
            headerMap[HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString(10)
            headerMap[HEADER_X_TKPD_APP_NAME] = GlobalConfig.getPackageApplicationName()
            headerMap[HEADER_X_TKPD_APP_VERSION] = "android-${GlobalConfig.VERSION_NAME}"
            headerMap[HEADER_OS_VERSION] = Build.VERSION.SDK_INT.toString(10)
            headerMap[HEADER_USER_AGENT] = getUserAgent()

            headerMap[HEADER_USER_ID] = userId
            headerMap[HEADER_DEVICE] = "android-${GlobalConfig.VERSION_NAME}"

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
            userSession: UserSessionInterface
        ): MutableMap<String, String> {
            val finalHeader = getDefaultHeaderMap(
                path,
                strParam,
                method,
                contentType ?: CONTENT_TYPE,
                authKey,
                DATE_FORMAT,
                userSession
            )

            finalHeader[HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString(10)

            return finalHeader
        }

        @JvmStatic
        fun generateHeadersAccount(authKey: String): MutableMap<String, String> {
            val clientID = "7ea919182ff"
            val clientSecret = "b36cbf904d14bbf90e7f25431595a364"
            val encodeString = "${clientID}:${clientSecret}"

            val finalHeader = HashMap<String, String>()
            finalHeader[HEADER_CONTENT_TYPE] = CONTENT_TYPE
            finalHeader[HEADER_CACHE_CONTROL] = "no-cache"

            if (authKey.isEmpty()) {
                finalHeader[HEADER_AUTHORIZATION] =
                    "Basic ${AuthHelperJava.base64Encoder(encodeString, Base64.NO_WRAP)}"
            } else {
                finalHeader[HEADER_AUTHORIZATION] = authKey
            }

            finalHeader.remove(HEADER_RELEASE_TRACK)
            finalHeader[HEADER_RELEASE_TRACK] = GlobalConfig.VERSION_NAME_SUFFIX
            finalHeader[HEADER_DEVICE] = "android-${GlobalConfig.VERSION_NAME}"
            finalHeader[HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString(10)
            finalHeader[HEADER_X_TKPD_APP_NAME] = GlobalConfig.getPackageApplicationName()
            finalHeader[HEADER_X_TKPD_APP_VERSION] = "android-${GlobalConfig.VERSION_NAME}"

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
                contentType ?: CONTENT_TYPE,
                authKey,
                DATE_FORMAT,
                userId,
                userSessionInterface
            ) as ArrayMap<String, String>

            finalHeader[HEADER_X_APP_VERSION] = GlobalConfig.VERSION_CODE.toString(10)
            finalHeader[HEADER_PATH] = path

            return finalHeader
        }

        @JvmStatic
        fun generateParamsNetwork(
            userId: String,
            deviceId: String,
            params: MutableMap<String, String>
        ): MutableMap<String, String> {
            val hash = getMD5Hash("${userId}~${deviceId}")

            params[PARAM_USER_ID] = userId
            params[PARAM_DEVICE_ID] = deviceId
            params[PARAM_HASH] = hash
            params[PARAM_OS_TYPE] = "1"
            params[PARAM_TIMESTAMP] = (Date().time / 1000).toString(10)

            return params
        }

        @JvmStatic
        fun calculateRFC2104HMAC(authString: String, authKey: String): String {
            return try {
                val signingKey = SecretKeySpec(authKey.toByteArray(), MAC_ALGORITHM)
                val mac = Mac.getInstance(MAC_ALGORITHM)
                mac.init(signingKey)
                val rawHmac = mac.doFinal(authString.toByteArray())
                AuthHelperJava.base64Encoder(rawHmac, Base64.DEFAULT)
            } catch (e: Exception) {
                logException(e)
                ""
            }
        }

        private fun logException(e: Exception) {
            val messageMap: MutableMap<String, String> = HashMap()
            messageMap["type"] = "exception"
            messageMap["exception"] = e.javaClass.name
            messageMap["stack_trace"] = Log.getStackTraceString(e).take(1000)
            ServerLogger.log(Priority.P2, "AUTH_HELPER", messageMap)
        }

        @JvmStatic
        fun getMD5Hash(raw: String): String {
            return AuthHelperJava.getMD5Hash(raw)
        }

        @JvmStatic
        fun generateDate(dateFormat: String): String {
            return try {
                val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
                simpleDateFormat.format(Date())
            } catch (e: Exception) {
                logException(e)
                ""
            }
        }

        @JvmStatic
        fun getUserAgent(): String {
            return "TkpdConsumer/${GlobalConfig.VERSION_NAME} (Android ${Build.VERSION.RELEASE};)"
        }
    }
}
