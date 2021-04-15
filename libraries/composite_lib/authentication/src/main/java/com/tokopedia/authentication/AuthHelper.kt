package com.tokopedia.authentication

import android.os.Build
import android.util.Base64
import androidx.collection.ArrayMap
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.user.session.UserSessionInterface
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class AuthHelper {
    companion object {

        val p = Pattern.compile("([\\d]*[^-\\w][\\d]*)-([\\D]*)")

        const val ERROR = "error read version name"

        @JvmStatic
        fun getVersionName(versionName: String): Pair<String, String> {
            val m = p.matcher(versionName)
            if(m.groupCount()==2){
                while(m.find()){
                    return Pair(m.group(1), m.group(2))
                }
            }
            return Pair(ERROR, ERROR)
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
            headerMap[HEADER_ACCOUNT_AUTHORIZATION] = "$HEADER_PARAM_BEARER ${userSession.accessToken}"
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
                finalHeader[HEADER_AUTHORIZATION] = "Basic ${AuthHelperJava.base64Encoder(encodeString, Base64.NO_WRAP)}"
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
            try {
                val signingKey = SecretKeySpec(authKey.toByteArray(), MAC_ALGORITHM)
                val mac = Mac.getInstance(MAC_ALGORITHM)
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
        fun getMD5Hash(raw: String): String {
            return AuthHelperJava.getMD5Hash(raw)
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
