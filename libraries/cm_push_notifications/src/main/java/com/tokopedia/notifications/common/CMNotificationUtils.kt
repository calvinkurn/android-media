package com.tokopedia.notifications.common

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.track.TrackApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URLDecoder
import java.net.UnknownHostException
import java.util.*
import kotlin.ClassCastException
import kotlin.coroutines.CoroutineContext

/**
 * Created by Ashwani Tyagi on 24/10/18.
 */
object CMNotificationUtils {

    internal var TAG = CMNotificationUtils::class.java.simpleName

    internal val STATE_LOGGED_OUT = "LOGGED_OUT"
    internal val STATE_LOGGED_IN = "LOGGED_IN"

    val CUSTOMER_APP_PAKAGE = "com.tokopedia.tkpd"
    val CUSTOMER_APP_NAME = "Tokopedia"
    val SELLER_APP_PAKAGE = "com.tokopedia.sellerapp"
    val SELLER_APP_NAME = "seller"
    val MITRA_APP_PAKAGE = "com.tokopedia.kelontongapp"
    val MITRA_APP_NAME = "mitra"

    val currentLocalTimeStamp: Long
        get() = System.currentTimeMillis()

    val sdkVersion: Int
        get() = Build.VERSION.SDK_INT


    private fun tokenUpdateRequired(newToken: String, cacheHandler: CMNotificationCacheHandler): Boolean {
        val oldToken = cacheHandler.getStringValue(CMConstant.FCM_TOKEN_CACHE_KEY)
        if (TextUtils.isEmpty(oldToken)) {
            return true

        } else if (oldToken == newToken) {
            return false
        }
        return true
    }

    fun isTokenExpired(cacheHandler: CMNotificationCacheHandler, newToken: String, userId: String, gAdId: String, appVersionName: String): Boolean {
        return tokenUpdateRequired(newToken, cacheHandler) ||
                mapTokenWithUserRequired(userId, cacheHandler) ||
                mapTokenWithGAdsIdRequired(gAdId, cacheHandler) ||
                mapTokenWithAppVersionRequired(appVersionName, cacheHandler)
    }

    fun getUserIdAndStatus(context: Context, userId: String): Pair<String, Int> {
        val cacheHandler = CMNotificationCacheHandler(context)
        val oldUserId = cacheHandler.getStringValue(CMConstant.USERID_CACHE_KEY)
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
                userIdInt = Integer.parseInt(userId.trim { it <= ' ' })
            } catch (e: NumberFormatException) {
            }
        }
        return userIdInt
    }


    private fun mapTokenWithUserRequired(newUserId: String, cacheHandler: CMNotificationCacheHandler): Boolean {
        val oldUserID = cacheHandler.getStringValue(CMConstant.USERID_CACHE_KEY)
        if (TextUtils.isEmpty(oldUserID)) {
            return !TextUtils.isEmpty(newUserId)
        } else if (!TextUtils.isEmpty(newUserId)) {
            return newUserId != oldUserID
        } else if (!TextUtils.isEmpty(oldUserID)) {
            return TextUtils.isEmpty(newUserId)
        }
        return false
    }

    private fun mapTokenWithGAdsIdRequired(gAdsId: String, cacheHandler: CMNotificationCacheHandler): Boolean {
        val oldGAdsId = cacheHandler.getStringValue(CMConstant.GADSID_CACHE_KEY)
        if (TextUtils.isEmpty(gAdsId)) {
            return false

        } else if (gAdsId == oldGAdsId) {
            return false
        }
        return true
    }

    private fun mapTokenWithAppVersionRequired(appVersionName: String, cacheHandler: CMNotificationCacheHandler): Boolean {
        val oldAppVersionName = try {
            cacheHandler.getStringValue(CMConstant.APP_VERSION_CACHE_KEY)
        } catch (e: ClassCastException) {
            try {
                cacheHandler.remove(CMConstant.APP_VERSION_CACHE_KEY)
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
        val cacheHandler = CMNotificationCacheHandler(context)
        var appId = cacheHandler.getStringValue(CMConstant.UNIQUE_APP_ID_CACHE_KEY)
        if (TextUtils.isEmpty(appId)) {
            appId = UUID.randomUUID().toString()
            cacheHandler.saveStringValue(CMConstant.UNIQUE_APP_ID_CACHE_KEY, appId)
        }
        return appId ?: ""
    }

    fun saveToken(context: Context, token: String) {
        val cacheHandler = CMNotificationCacheHandler(context)
        cacheHandler.saveStringValue(CMConstant.FCM_TOKEN_CACHE_KEY, token)
    }

    fun getToken(context: Context): String? = CMNotificationCacheHandler(context).getStringValue(CMConstant.FCM_TOKEN_CACHE_KEY)


    fun saveUserId(context: Context, userId: String) {
        val cacheHandler = CMNotificationCacheHandler(context)
        cacheHandler.saveStringValue(CMConstant.USERID_CACHE_KEY, userId)
    }

    fun getUserId(context: Context): String? = CMNotificationCacheHandler(context).getStringValue(CMConstant.USERID_CACHE_KEY)

    fun saveGAdsIdId(context: Context, gAdsId: String) {
        val cacheHandler = CMNotificationCacheHandler(context)
        cacheHandler.saveStringValue(CMConstant.GADSID_CACHE_KEY, gAdsId)
    }

    fun saveAppVersion(context: Context, versionName: String) {
        val cacheHandler = CMNotificationCacheHandler(context)
        cacheHandler.saveStringValue(CMConstant.APP_VERSION_CACHE_KEY, versionName)
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

    fun loadBitmapFromUrl(imageUrl: String?): Bitmap? {
        if (imageUrl == null || imageUrl.length == 0) {
            return null
        }
        var bitmap: Bitmap? = null
        try {
            val inputStream = java.net.URL(imageUrl).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
        } catch (e: OutOfMemoryError) {
            Log.e(TAG, String.format("Out of Memory Error in image bitmap download for Url: %s.", imageUrl))
            return null
        } catch (e: UnknownHostException) {
            Log.e(TAG, String.format("Unknown Host Exception in image bitmap download for Url: %s. Device " + "may be offline.", imageUrl))
            return null
        } catch (e: MalformedURLException) {
            Log.e(TAG, String.format("Malformed URL Exception in image bitmap download for Url: %s. Image " + "Url may be corrupted.", imageUrl))
            return null
        } catch (e: Exception) {
            Log.e(TAG, String.format("Exception in image bitmap download for Url: %s", imageUrl))
            return null
        }

        return bitmap
    }

    fun hasActionButton(baseNotificationModel: BaseNotificationModel): Boolean {
        return baseNotificationModel.actionButton.isNotEmpty()

    }


    fun getSpannedTextFromStr(str: String?): Spanned {
        if (null == str)
            return SpannableStringBuilder("")
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(str)
            }
        } catch (e: Exception) {
            return SpannableStringBuilder(str)
        }

    }

    fun getPXtoDP(context: Context, dip: Float) = dip * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)


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

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun isValidCampaignUrl(uri: Uri): Boolean {
        val maps: Map<String, String>? = splitQuery(uri)
        maps?.let {
            return it.containsKey(CMConstant.UTMParams.UTM_GCLID) ||
                    it.containsKey(CMConstant.UTMParams.UTM_SOURCE) &&
                    it.containsKey(CMConstant.UTMParams.UTM_MEDIUM) &&
                    it.containsKey(CMConstant.UTMParams.UTM_CAMPAIGN)
        } ?: return false

    }


    private fun splitQuery(url: Uri): MutableMap<String, String>? {
        val queryPairs: MutableMap<String, String> = LinkedHashMap()
        val query = url.query
        if (!TextUtils.isEmpty(query)) {
            val pairs = query!!.split("&|\\?".toRegex()).toTypedArray()
            for (pair in pairs) {
                val indexKey = pair.indexOf("=")
                if (indexKey > 0 && indexKey + 1 <= pair.length) {
                    try {
                        queryPairs[URLDecoder.decode(pair.substring(0, indexKey), "UTF-8")] = URLDecoder.decode(pair.substring(indexKey + 1), "UTF-8")
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return queryPairs
    }

    fun sendUTMParamsInGTM(appLink: String?) {
        val uri = Uri.parse(appLink)
        if (!isValidCampaignUrl(uri))
            return

        val campaign = splitQuery(uri)
        campaign?.let {
            if (!it.containsKey(CMConstant.UTMParams.UTM_TERM) || it[CMConstant.UTMParams.UTM_TERM] == null)
                it[CMConstant.UTMParams.UTM_TERM] = ""
            if (!it.containsKey(CMConstant.UTMParams.SCREEN_NAME) || it[CMConstant.UTMParams.SCREEN_NAME] == null)
                it[CMConstant.UTMParams.SCREEN_NAME] = CMConstant.UTMParams.SCREEN_NAME_VALUE
        }
        TrackApp.getInstance().gtm.sendCampaign(campaign as Map<String, Any>?)
    }


    fun isDarkMode(context: Context): Boolean {
        return try {
            when (context.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                Configuration.UI_MODE_NIGHT_UNDEFINED -> false
                else -> false
            }
        } catch (ignored: Exception) {
            false
        }
    }


    fun checkTokenValidity(token: String): Boolean {
        return token.length <= 36
    }
}

fun CoroutineScope.launchCatchError(context: CoroutineContext = coroutineContext,
                                    block: suspend (() -> Unit),
                                    onError: (Throwable) -> Unit) =
        launch(context) {
            try {
                block.invoke()
            } catch (t: Throwable) {
                onError(t)
            }
        }
