package com.tokopedia.fcmcommon.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import com.tokopedia.fcmcommon.common.FcmCacheHandler
import com.tokopedia.fcmcommon.common.FcmConstant
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext

object FcmTokenUtils {

    private const val STATE_LOGGED_OUT = "LOGGED_OUT"
    private const val STATE_LOGGED_IN = "LOGGED_IN"

    private const val CUSTOMER_APP_PAKAGE = "com.tokopedia.tkpd"
    private const val CUSTOMER_APP_NAME = "Tokopedia"
    private const val SELLER_APP_PAKAGE = "com.tokopedia.sellerapp"
    const val SELLER_APP_NAME = "seller"
    private const val MITRA_APP_PAKAGE = "com.tokopedia.kelontongapp"
    private const val MITRA_APP_NAME = "mitra"
    private const val CACHE_CM_NOTIFICATIONS = "cache_fcmnotifications"

    val currentLocalTimeStamp: Long
        get() = System.currentTimeMillis()

    val sdkVersion: Int
        get() = Build.VERSION.SDK_INT

    private fun tokenUpdateRequired(newToken: String, cacheHandler: FcmCacheHandler): Boolean {
        val oldToken = cacheHandler.getStringValue(FcmConstant.FCM_TOKEN_CACHE_KEY)
        if (TextUtils.isEmpty(oldToken)) {
            return true
        } else if (oldToken == newToken) {
            return false
        }
        return true
    }

    fun isTokenExpired(cacheHandler: FcmCacheHandler, newToken: String, userId: String, gAdId: String, appVersionName: String): Boolean {
        return tokenUpdateRequired(newToken, cacheHandler) ||
            mapTokenWithUserRequired(userId, cacheHandler) ||
            mapTokenWithGAdsIdRequired(gAdId, cacheHandler) ||
            mapTokenWithAppVersionRequired(appVersionName, cacheHandler)
    }

    fun getUserIdAndStatus(context: Context, userId: String): Pair<String, Int> {
        val cacheHandler = FcmCacheHandler(context, CACHE_CM_NOTIFICATIONS)
        val oldUserId = cacheHandler.getStringValue(FcmConstant.USERID_CACHE_KEY)
        return if (TextUtils.isEmpty(userId)) {
            if (TextUtils.isEmpty(oldUserId)) {
                Pair("", getUserIdAsInt(userId))
            } else {
                Pair(STATE_LOGGED_OUT, getUserIdAsInt(oldUserId ?: "0"))
            }
        } else {
            Pair(STATE_LOGGED_IN, getUserIdAsInt(userId))
        }
    }

    private fun getUserIdAsInt(userId: String): Int {
        var userIdInt = 0
        if (!TextUtils.isEmpty(userId)) {
            try {
                userIdInt = userId.toIntOrZero()
            } catch (e: NumberFormatException) {
            }
        }
        return userIdInt
    }

    private fun mapTokenWithUserRequired(newUserId: String, cacheHandler: FcmCacheHandler): Boolean {
        val oldUserID = cacheHandler.getStringValue(FcmConstant.USERID_CACHE_KEY)
        if (TextUtils.isEmpty(oldUserID)) {
            return !TextUtils.isEmpty(newUserId)
        } else if (!TextUtils.isEmpty(newUserId)) {
            return newUserId != oldUserID
        } else if (!TextUtils.isEmpty(oldUserID)) {
            return TextUtils.isEmpty(newUserId)
        }
        return false
    }

    private fun mapTokenWithGAdsIdRequired(gAdsId: String, cacheHandler: FcmCacheHandler): Boolean {
        val oldGAdsId = cacheHandler.getStringValue(FcmConstant.GADSID_CACHE_KEY)
        if (TextUtils.isEmpty(gAdsId)) {
            return false
        } else if (gAdsId == oldGAdsId) {
            return false
        }
        return true
    }

    private fun mapTokenWithAppVersionRequired(appVersionName: String, cacheHandler: FcmCacheHandler): Boolean {
        val oldAppVersionName = try {
            cacheHandler.getStringValue(FcmConstant.APP_VERSION_CACHE_KEY)
        } catch (e: ClassCastException) {
            try {
                cacheHandler.removeString(FcmConstant.APP_VERSION_CACHE_KEY)
                ""
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        Timber.d("CMUser-APP_VERSION$oldAppVersionName#new-$appVersionName")
        return TextUtils.isEmpty(oldAppVersionName) || !oldAppVersionName.equals(appVersionName, ignoreCase = true)
    }

    fun getUniqueAppId(context: Context): String {
        val cacheHandler = FcmCacheHandler(context, CACHE_CM_NOTIFICATIONS)
        var appId = cacheHandler.getStringValue(FcmConstant.UNIQUE_APP_ID_CACHE_KEY)
        if (TextUtils.isEmpty(appId)) {
            appId = UUID.randomUUID().toString()
            cacheHandler.saveStringValue(FcmConstant.UNIQUE_APP_ID_CACHE_KEY, appId)
        }
        return appId ?: ""
    }

    fun saveToken(context: Context, token: String) {
        val cacheHandler = FcmCacheHandler(context, CACHE_CM_NOTIFICATIONS)
        cacheHandler.saveStringValue(FcmConstant.FCM_TOKEN_CACHE_KEY, token)
    }

    fun getToken(context: Context): String? = FcmCacheHandler(context, CACHE_CM_NOTIFICATIONS).getStringValue(FcmConstant.FCM_TOKEN_CACHE_KEY)

    fun saveUserId(context: Context, userId: String) {
        val cacheHandler = FcmCacheHandler(context, CACHE_CM_NOTIFICATIONS)
        cacheHandler.saveStringValue(FcmConstant.USERID_CACHE_KEY, userId)
    }

    fun getUserId(context: Context): String? = FcmCacheHandler(context, CACHE_CM_NOTIFICATIONS).getStringValue(FcmConstant.USERID_CACHE_KEY)

    fun saveGAdsIdId(context: Context, gAdsId: String) {
        val cacheHandler = FcmCacheHandler(context, CACHE_CM_NOTIFICATIONS)
        cacheHandler.saveStringValue(FcmConstant.GADSID_CACHE_KEY, gAdsId)
    }

    fun saveAppVersion(context: Context, versionName: String) {
        val cacheHandler = FcmCacheHandler(context, CACHE_CM_NOTIFICATIONS)
        cacheHandler.saveStringValue(FcmConstant.APP_VERSION_CACHE_KEY, versionName)
    }

    fun getCurrentAppVersionName(context: Context): String {
        var pInfo: PackageInfo? = null
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo!!.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return "NA"
    }

    fun getApplicationName(context: Context?): String {
        var appName = ""
        if (context != null) {
            val packageName = context.packageName
            if (CUSTOMER_APP_PAKAGE.equals(packageName, ignoreCase = true)) {
                appName = CUSTOMER_APP_NAME
            } else if (SELLER_APP_PAKAGE.equals(packageName, ignoreCase = true)) {
                appName = SELLER_APP_NAME
            }
            if (MITRA_APP_PAKAGE.equals(packageName, ignoreCase = true)) {
                appName = MITRA_APP_NAME
            }
        }
        return appName
    }

    fun checkTokenValidity(token: String): Boolean {
        return token.length <= 36
    }

    fun getWifiMacAddress(context: Context): String {
        val cacheHandler = FcmCacheHandler(context, CACHE_CM_NOTIFICATIONS)
        var macAddress = cacheHandler.getStringValue(FcmConstant.KEY_WIFI_MAC_ADDRESS)
        if (macAddress.isNullOrBlank() || macAddress == FcmDeviceConfig.UNKNOWN) {
            macAddress = FcmDeviceConfig.getFcmDeviceConfig().getWifiMAC()
            cacheHandler.saveStringValue(FcmConstant.KEY_WIFI_MAC_ADDRESS, macAddress)
        }
        return macAddress
    }
}

fun CoroutineScope.launchCatchError(
    context: CoroutineContext = coroutineContext,
    block: suspend (() -> Unit),
    onError: (Throwable) -> Unit
) =
    launch(context) {
        try {
            block.invoke()
        } catch (t: Throwable) {
            onError(t)
        }
    }
