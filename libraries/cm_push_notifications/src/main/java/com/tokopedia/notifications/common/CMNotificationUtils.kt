package com.tokopedia.notifications.common

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.notifications.model.BaseNotificationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.MalformedURLException
import java.net.UnknownHostException
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Ashwani Tyagi on 24/10/18.
 */
object CMNotificationUtils {

    internal var TAG = CMNotificationUtils::class.java.simpleName

    internal val STATE_LOGGED_OUT = "LOGGED_OUT"
    internal val STATE_LOGGED_IN = "LOGGED_IN"

    val CUSTOMER_APP_PAKAGE = "com.tokopedia.tkpd"
    val SELLER_APP_PAKAGE = "com.tokopedia.sellerapp"
    val MITRA_APP_PAKAGE = "com.tokopedia.kelontongapp"

    val currentLocalTimeStamp: Long
        get() = System.currentTimeMillis()

    val sdkVersion: Int
        get() = Build.VERSION.SDK_INT


    fun tokenUpdateRequired(context: Context, newToken: String): Boolean {
        val cacheHandler = CMNotificationCacheHandler(context)
        val oldToken = cacheHandler.getStringValue(CMConstant.FCM_TOKEN_CACHE_KEY)
        if (TextUtils.isEmpty(oldToken)) {
            return true

        } else if (oldToken == newToken) {
            return false
        }
        return true
    }

    fun getUserStatus(context: Context, userId: String): String {
        val cacheHandler = CMNotificationCacheHandler(context)
        val oldUserId = cacheHandler.getStringValue(CMConstant.USERID_CACHE_KEY)
        return if (TextUtils.isEmpty(userId)) {
            if (TextUtils.isEmpty(oldUserId)) {
                ""
            } else {
                STATE_LOGGED_OUT
            }
        } else {
            STATE_LOGGED_IN
        }
    }

    fun mapTokenWithUserRequired(context: Context, newUserId: String): Boolean {
        val cacheHandler = CMNotificationCacheHandler(context)
        val oldUserID = cacheHandler.getStringValue(CMConstant.USERID_CACHE_KEY)
        if (TextUtils.isEmpty(oldUserID)) {
            return !TextUtils.isEmpty(newUserId)
        } else if (!TextUtils.isEmpty(oldUserID)) {
            return TextUtils.isEmpty(newUserId)
        } else if (!TextUtils.isEmpty(newUserId)) {
            return newUserId != oldUserID
        }
        return false
    }

    fun mapTokenWithGAdsIdRequired(context: Context, gAdsId: String): Boolean {
        val cacheHandler = CMNotificationCacheHandler(context)
        val oldGAdsId = cacheHandler.getStringValue(CMConstant.GADSID_CACHE_KEY)
        if (TextUtils.isEmpty(gAdsId)) {
            return false

        } else if (gAdsId == oldGAdsId) {
            return false
        }
        return true
    }

    fun mapTokenWithAppVersionRequired(context: Context, appVersionName: String): Boolean {
        val cacheHandler = CMNotificationCacheHandler(context)
        val oldAppVersionName = cacheHandler.getStringValue(CMConstant.APP_VERSION_CACHE_KEY)
        CommonUtils.dumper("CMUser-APP_VERSION$oldAppVersionName#new-$appVersionName")
        return if (TextUtils.isEmpty(oldAppVersionName))
            true
        else if (oldAppVersionName.equals(appVersionName, ignoreCase = true)) {
            false
        } else
            false
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

    fun saveUserId(context: Context, userId: String) {
        val cacheHandler = CMNotificationCacheHandler(context)
        cacheHandler.saveStringValue(CMConstant.USERID_CACHE_KEY, userId)
    }

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
        } catch (e: UnknownHostException) {
            Log.e(TAG, String.format("Unknown Host Exception in image bitmap download for Url: %s. Device " + "may be offline.", imageUrl))
        } catch (e: MalformedURLException) {
            Log.e(TAG, String.format("Malformed URL Exception in image bitmap download for Url: %s. Image " + "Url may be corrupted.", imageUrl))
        } catch (e: Exception) {
            Log.e(TAG, String.format("Exception in image bitmap download for Url: %s", imageUrl))
        }

        return bitmap
    }

    fun hasActionButton(baseNotificationModel: BaseNotificationModel): Boolean {
        return baseNotificationModel.actionButton != null && baseNotificationModel.actionButton.size > 0

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

    fun getApplicationName(context: Context?): String {
        var appName = ""
        if (context != null) {
            val packageName = context.packageName
            if (CUSTOMER_APP_PAKAGE.equals(packageName, ignoreCase = true)) {
                appName = "Tokopedia"
            } else if (SELLER_APP_PAKAGE.equals(packageName, ignoreCase = true)) {
                appName = "seller"
            }
            if (MITRA_APP_PAKAGE.equals(packageName, ignoreCase = true)) {
                appName = "mitra"
            }
        }
        return appName
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
